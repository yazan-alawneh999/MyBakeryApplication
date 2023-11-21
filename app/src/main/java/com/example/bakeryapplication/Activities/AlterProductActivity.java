package com.example.bakeryapplication.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.ActivityAlterProductBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Internal;

public class AlterProductActivity extends AppCompatActivity {
    ActivityAlterProductBinding AB;
    ActivityResultLauncher<Intent> pic_launcher;
    Uri productImageUri;
    String prdID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set window flags
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // xml binding
        AB = ActivityAlterProductBinding.inflate(getLayoutInflater());

        setContentView(AB.getRoot());
        // set tool bar
        setSupportActionBar(AB.alterTb);
        // register  launcher
        pic_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent pic = result.getData();
                        if (pic != null && pic.getData() != null) {
                            productImageUri = pic.getData();
                            Glide
                                    .with(this)
                                    .load(productImageUri)
                                    .into(AB.alterIv);

                        }

                    }

                });
        // get pic for new product
        AB.alterIv.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(512, 512)
                    .createIntent(intent -> {
                        pic_launcher.launch(intent);
                        return null;
                    });
        });
        // check add or alter product
        if (getProductIntent() != null) {
            prdID = getProductIntent().getId();

        }
        if (isEditingProcess(prdID)) {
            Log.d("yazan", "is editing process :");
            ProductModel product = getProductIntent();
            if (product != null)
                Log.d("yazan", "" + product.getName());
            fillProductInfo(product);
            disableFields();
        } else {
            enableFields();
        }
        // navigation button
        AB.alterTb.setNavigationOnClickListener(v -> {
            BakeryHelper.returnToHome(this);

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alter_activity_menu, menu);
        if (isEditingProcess(prdID)) {
            // edit product
            setEditOptionsMenu(menu);
        } else {
            // add product
            setAddOptionsMenu(menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!validateTime() | !validateCalories() | !validateFee() | !validateName() | !validDescription() | !validateType()) {
            return false;
        }
        ProductModel newProduct = getProductFromAdmin();
        // save button
        if (item.getItemId() == R.id.done_menu_button) {
            if (isEditingProcess(prdID)) {
                Log.d("yazan", "is editing process :");
                Log.d("yazan", prdID);
                if (newProduct != null)
                {
                    Log.d("yazan", newProduct.getName());
                    FirebaseHelper.getRecommandedImages(newProduct.getName())
                            .putFile(Uri.parse(newProduct.getPic()))
                            .addOnSuccessListener(taskSnapshot ->
                            {
                                if (taskSnapshot.getTask().isSuccessful()) {
                                    Log.d("yazan", "put picture success: ");

                                    updateProduct(prdID, newProduct);
                                    BakeryHelper.returnToHome(getApplicationContext());
                                }else
                                    Log.d("yazan", "put picture failed: ");

                            });
                }
                return true;

            } else {
                FirebaseHelper.getRecommandedImages(newProduct.getName())
                        .putFile(Uri.parse(newProduct.getPic()))
                        .addOnSuccessListener(taskSnapshot -> {
                            saveProduct(getProductFromAdmin());
                            BakeryHelper.returnToHome(getApplicationContext());
                        });

                return true;
            }


        }

        // edit button
        else if (item.getItemId() == R.id.edit_ulter_menu) {
            enableFields();
            AB.alterTb.getMenu().findItem(R.id.done_menu_button).setVisible(true);
            AB.alterTb.getMenu().findItem(R.id.delete_product_icon).setVisible(false);
            return true;

        }

        //delete button
        else if (item.getItemId() == R.id.delete_product_icon) {
            deleteProduct(prdID);
            return true;
        }

        return false;
    }

    private void deleteProduct(String prdID) {
        FirebaseHelper.getNewProductsDocs().document(prdID)
                .delete()
                .addOnSuccessListener(unused -> {
                    BakeryHelper.showToast(this, "product delete success");
                    BakeryHelper.returnToHome(getApplicationContext());
                });
    }

    public void updateProduct(String productId, ProductModel product) {
        FirebaseHelper.getNewProductsDocs().document(productId)
                .set(product)
                .addOnSuccessListener(unused -> {
                    BakeryHelper.showToast(this, "product update success");

                });
    }

    private void saveProduct(ProductModel productFromAdmin) {
        productFromAdmin.setRate(5);
        FirebaseHelper.getNewProductsDocs()
                .add(productFromAdmin)
                .addOnSuccessListener(documentReference ->
                {
                    BakeryHelper.showToast(this, "product added ");

                });
    }

    public boolean isRecommended() {
        if (AB.recommandedTrueRadioButton.isChecked()) {
            return true;
        }
        return false;
    }

    public ProductModel getProductFromAdmin() {

        String name, description, type;
        int prepareTime;
        int calories;
        double fee;
        boolean isRecommanded = isRecommended();


        name = AB.alertProductNameInLayout.getEditText().getText().toString();
        description = AB.alertProductDescInLayout.getEditText().getText().toString();
        type = AB.alertProductTypeInLayout.getEditText().getText().toString();
        prepareTime = Integer.parseInt(AB.alertProductTimeInLayout.getEditText().getText().toString());
        calories = Integer.parseInt(AB.alertProductCalInLayout.getEditText().getText().toString());
        fee = Double.parseDouble(AB.alertProductFeeInLayout.getEditText().getText().toString());

        ProductModel product = new ProductModel(String.valueOf(productImageUri), name, description, type,
                prepareTime, calories, fee, isRecommanded);
        return product;


    }

    public boolean isEditingProcess(String prodId) {
        if (prodId != null) {
            return true;
        }
        return false;
    }

    public ProductModel getProductIntent() {
        ProductModel prod = (ProductModel) getIntent().getSerializableExtra("productId");
        return prod;
    }

    public void setEditOptionsMenu(Menu menu) {
        menu.findItem(R.id.edit_ulter_menu).setVisible(true);
        menu.findItem(R.id.delete_product_icon).setVisible(true);
        menu.findItem(R.id.done_menu_button).setVisible(false);
    }

    public void setAddOptionsMenu(Menu menu) {
        menu.findItem(R.id.edit_ulter_menu).setVisible(false);
        menu.findItem(R.id.delete_product_icon).setVisible(false);
        menu.findItem(R.id.done_menu_button).setVisible(true);
    }

    private void setImageFromCloud(String itemName, ImageView imageItem) {
        FirebaseHelper.getRecommandedImages(itemName).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(getApplicationContext()).load(uri).into(imageItem);

                });
    }

    private void fillProductInfo(ProductModel product) {
        setImageFromCloud(product.getName(), AB.alterIv);
        AB.alertProductNameInLayout.getEditText().setText(product.getName());
        AB.alertProductDescInLayout.getEditText().setText(product.getDescription());
        AB.alertProductTypeInLayout.getEditText().setText(product.getType());
        AB.alertProductFeeInLayout.getEditText().setText(String.valueOf(product.getFee()));
        AB.alertProductTimeInLayout.getEditText().setText(product.getPrepareTime() + "");
        AB.alertProductCalInLayout.getEditText().setText(product.getCalories() + "");

    }

    private void disableFields() {
        AB.alterIv.setEnabled(false);
        AB.alertProductNameInLayout.getEditText().setEnabled(false);
        AB.alertProductDescInLayout.getEditText().setEnabled(false);
        AB.alertProductTypeInLayout.getEditText().setEnabled(false);
        AB.alertProductFeeInLayout.getEditText().setEnabled(false);
        AB.alertProductTimeInLayout.getEditText().setEnabled(false);
        AB.alertProductCalInLayout.getEditText().setEnabled(false);
        AB.recommandedTrueRadioButton.setEnabled(false);
        AB.recommandedFalseRadioButton.setEnabled(false);

    }

    private void enableFields() {
        AB.alterIv.setEnabled(true);
        AB.alertProductNameInLayout.getEditText().setEnabled(true);
        AB.alertProductDescInLayout.getEditText().setEnabled(true);
        AB.alertProductTypeInLayout.getEditText().setEnabled(true);
        AB.alertProductFeeInLayout.getEditText().setEnabled(true);
        AB.alertProductTimeInLayout.getEditText().setEnabled(true);
        AB.alertProductCalInLayout.getEditText().setEnabled(true);
        AB.recommandedTrueRadioButton.setEnabled(true);
        AB.recommandedFalseRadioButton.setEnabled(true);

    }

    private Boolean validateName() {
        String val = AB.alertProductNameInLayout.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            AB.alertProductNameInLayout.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            AB.alertProductNameInLayout.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            AB.alertProductNameInLayout.setError("White Spaces are not allowed");
            return false;
        } else {
            AB.alertProductNameInLayout.setError(null);
            AB.alertProductNameInLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateTime() {
        String val = AB.alertProductTimeInLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            AB.alertProductTimeInLayout.setError("Field cannot be empty");
            return false;
        } else {
            AB.alertProductTimeInLayout.setError(null);
            AB.alertProductTimeInLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCalories() {
        String val = AB.alertProductCalInLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            AB.alertProductCalInLayout.setError("Field cannot be empty");
            return false;

        } else {
            AB.alertProductCalInLayout.setError(null);
            AB.alertProductCalInLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateFee() {
        String val = AB.alertProductFeeInLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            AB.alertProductFeeInLayout.setError("Field cannot be empty");
            return false;

        } else {
            AB.alertProductFeeInLayout.setError(null);
            AB.alertProductFeeInLayout.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validDescription() {
        String val = AB.alertProductDescInLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            AB.alertProductDescInLayout.setError("Field cannot be empty");
            return false;

        } else {
            AB.alertProductDescInLayout.setError(null);
            AB.alertProductDescInLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateType() {
        String val = AB.alertProductTypeInLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            AB.alertProductTypeInLayout.setError("Field cannot be empty");
            return false;

        } else {
            AB.alertProductTypeInLayout.setError(null);
            AB.alertProductTypeInLayout.setErrorEnabled(false);
            return true;
        }


    }
}



