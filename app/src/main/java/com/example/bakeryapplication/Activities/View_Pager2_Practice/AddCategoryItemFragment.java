package com.example.bakeryapplication.Activities.View_Pager2_Practice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCategoryItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//  tab layout  lunch code
//// create fragments list
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(new AddCategoryItemFragment());
//        fragments.add(new AddRecommandedItemFragment());
//        // set tab layout adapter
//        NewProductTabLayoutAdapter adapter = new NewProductTabLayoutAdapter(this,fragments);
//        addP.AddProductViewPager.setAdapter(adapter);
//        // bind view pager with tab layout
//        new TabLayoutMediator(addP.tabLayout, addP.AddProductViewPager, (tab, position) -> {
//        // category label list
//        ArrayList<String> categoryLabel = new ArrayList<>();
//        categoryLabel.add(getString(R.string.addNewCategoryItemLabel));
//        categoryLabel.add(getString(R.string.addNewRecommandedItem));
//        //configure tab settings
//        tab.setText(categoryLabel.get(position));
//
//
//        }).attach();
public class AddCategoryItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCategoryItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCategoryItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCategoryItemFragment newInstance(String param1, String param2) {
        AddCategoryItemFragment fragment = new AddCategoryItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_category_item, container, false);
    }
}