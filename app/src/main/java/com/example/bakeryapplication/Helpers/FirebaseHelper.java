package com.example.bakeryapplication.Helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Models.ChatRoomModel;
import com.example.bakeryapplication.Models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseHelper {
    private static final String adminId = "Kt0tSbbX00TAouqfqIosLqqzjQc2";
    public static final String CHAT_ROOM_ID_KEY = "chatRoomId";
    public static final String REQ_USER_ID = "otherUserId";
    public static final String OTHER_USER_TOKEN = "other_user_token";
    private static final String CURRENT_USER_COLLECTION = "CurrentUser";
    private static final String ADD_TO_CART_COLLECTION = "addToCart";



    public static StorageReference getRecommandedImages(String productName){
        return FirebaseStorage.getInstance().getReference().child("recommandedImages").child(productName);
    }

    public static CollectionReference getAddCartRef() {
        return FirebaseFirestore.getInstance().collection(CURRENT_USER_COLLECTION)
                .document(getCurrentUserId())
                .collection(ADD_TO_CART_COLLECTION);

    }

    public static CollectionReference getNewProductsDocs() {
        return FirebaseFirestore.getInstance().collection("newProducts");

    }

    public static DatabaseReference getRealDatabaseUserRef() {
        return FirebaseDatabase.getInstance().getReference().child("users");

    }

    public static CollectionReference getUserCollectionRef() {
        return FirebaseFirestore.getInstance().collection("user");
    }

    public static DocumentReference getCurrentUserRef() {
        return FirebaseFirestore.getInstance().collection("user")
                .document(getCurrentUserId());
    }

    public static String getAdminId() {
        return adminId;

    }

    public static String chatRoomId(String senderId, String receiverId) {
        if (senderId.hashCode() < getAdminId().hashCode())
            return senderId + "_" + receiverId;
        else
            return receiverId + "_" + senderId;
    }

    public static DocumentReference getOtherUserFromChatRoom(List<String> userIds) {
        if (userIds.get(0).equals(FirebaseHelper.getCurrentUserId())) {
            return getUserCollectionRef().document(userIds.get(1));
        } else
            return getUserCollectionRef().document(userIds.get(0));


    }

    public static DocumentReference getChatRoomRef(String chatRoomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);

    }

    public static CollectionReference getAllChatRoomsRef() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static CollectionReference getChatRoomMessages(String chatRoomId) {
        return getChatRoomRef(chatRoomId).collection("chats");

    }

    public static void setCurrentUserInfo(UserModel user) {
        getUserCollectionRef().document(getCurrentUserId()).set(user);
    }


    public static StorageReference getProfilePictureReference() {
        return FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(getAuthInstance().getCurrentUser().getUid());

    }
    public static StorageReference getRecommandedPictureReference() {
        return FirebaseStorage.getInstance()
                .getReference()
                .child(getAuthInstance().getCurrentUser().getUid())
                .child("recommanded_pic");

    }

    public static Task<QuerySnapshot> getKaakCategory() {
        return getNewProductsDocs().whereEqualTo("type", "kaak").get();


    }

    public static Task<QuerySnapshot> getMaamoulCategory() {
        return getNewProductsDocs().whereEqualTo("type", "maamoul").get();


    }

    public static Task<QuerySnapshot> getHaraiesCategory() {
        return getNewProductsDocs().whereEqualTo("type", "haraies").get();


    }

    public static Task<QuerySnapshot> getPastriesCategory() {
        return getNewProductsDocs().whereEqualTo("type", "pastries").get();


    }

    public static CollectionReference getUserOrderRef() {
        return FirebaseFirestore.getInstance()
                .collection("CurrentUser")
                .document(getCurrentUserId())
                .collection("MyOrder");


    }

    public static CollectionReference getUserCartRef() {
        return FirebaseFirestore.getInstance()
                .collection("CurrentUser")
                .document(getCurrentUserId())
                .collection("addToCart");


    }

    public static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static CollectionReference getCategoriesRvReference() {
        return FirebaseFirestore.getInstance().collection("CategoriesRvModel");
    }

    public static CollectionReference getFragmentCategoriesModelRef() {
        return FirebaseFirestore.getInstance().collection("FragmentCategoriesModel");
    }

    public static CollectionReference getRecommandedRvReference() {
        return FirebaseFirestore.getInstance().collection("RecommandedRvModel");
    }

    public static void logout() {

        getAuthInstance().signOut();
    }

    public static void passReqUserChatRoomAsIntent(Intent intent, String chatRoomId, String otherUserId, String otherUserToken) {
        intent.putExtra(CHAT_ROOM_ID_KEY, chatRoomId);
        intent.putExtra(REQ_USER_ID, otherUserId);
        intent.putExtra(OTHER_USER_TOKEN, otherUserToken);

    }

    public static void setProductPicture(Context context , ImageView prodIv,String productName)
    {
        FirebaseHelper.getRecommandedImages(productName)
                .getDownloadUrl()
                .addOnSuccessListener(uri ->
                {
                    Glide.with(context)
                            .load(uri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(prodIv);
                });
    }

}
