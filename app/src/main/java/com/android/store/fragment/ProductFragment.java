package com.android.store.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.store.MainActivity;
import com.android.store.R;
import com.android.store.adapter.ProductAdapter;
import com.android.store.adapter.ProductSearchAdapter;
import com.android.store.adapter.SlidePhotoAdapter;
import com.android.store.model.Product;
import com.android.store.model.SlidePhoto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ProductFragment extends Fragment {

    private MainActivity home;
    private Timer mTimer;
    private List<SlidePhoto> listSlidePhoto;
    private List<Product> listAllProduct;

    private View mView;
    private RecyclerView rcvProduct;
    private ViewPager viewPagerSlidePhoto;
    private CircleIndicator circleIndicator;
    private AutoCompleteTextView atcProductSearch;
    private NestedScrollView nestedScrollViewProduct;
    private ProductAdapter productAdapter;
    private SlidePhotoAdapter slidePhotoAdapter;

    private int limitProduct = 8;
    private int startProduct = 1;
    public ProductFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        initItem();

        setDataSlidePhotoAdapter();

        setDataProductAdapter();

//        ArrayList<Product> listProduct = new ArrayList<>();
//        listProduct.add(new Product("https://img.fpt.shop/w_214/h_214/cmpr_10/m_letterbox_ffffff_100/https://fptshop.com.vn/Uploads/Originals/2020/8/5/637322682439532348_ss-note-20-ultra-5g-gold-dd.png", "Samsung Galaxy Note 20 Ultra", "Samsung Galaxy Note 20 Ultra ???????c ch??? t??c t??? nh???ng v???t li???u cao c???p h??ng ?????u hi???n nay, v???i s??? t??? m??? v?? ch???t l?????ng gia c??ng th?????ng th???a, t???o n??n chi???c ??i???n tho???i ?????p h??n nh???ng g?? b???n c?? th??? t?????ng t?????ng. Kh??ng ch??? c?? ki???u d??ng thanh l???ch, m??n h??nh kh??ng vi???n Infinity-O quy???n r??, Galaxy Note20 Ultra c??n th??? hi???n s??? cao c???p ??? t???ng chi ti???t nh??? nh?? c??c ph???n vi???n c???nh s??ng b??ng, h???a ti???t phay x?????c ?????c ????o tr??n khung m??y, mang ?????n ni???m c???m h???ng cho ng?????i d??ng ??? m???i g??c c???nh.", "samsung", 26990000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/206176/samsung-galaxy-note-10-plus-silver-new-600x600.jpg", "Samsung Galaxy Note 10+", "Tr??ng ngo???i h??nh kh?? gi???ng nhau, tuy nhi??n Samsung Galaxy Note 10+ s??? h???u kh?? nhi???u ??i???m kh??c bi???t so v???i Galaxy Note 10 v?? ????y ???????c xem l?? m???t trong nh???ng chi???c m??y ????ng mua nh???t trong n??m 2019, ?????c bi???t d??nh cho nh???ng ng?????i th??ch m???t chi???c m??y m??n h??nh l???n, camera ch???t l?????ng h??ng ?????u.", "samsung", 16490000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/228744/iphone-12-pro-max-vang-new-600x600-600x600.jpg", "iPhone 12 Pro Max","iPhone 12 Pro Max 512GB - ?????ng c???p t??? t??n g???i ?????n t???ng chi ti???t. Ngay t??? khi ch??? l?? tin ?????n th?? chi???c smartphone n??y ???? l??m ?????ng ng???i kh??ng y??n bao ???fan c???ng??? nh?? Apple, v???i nh???ng n??ng c???p v?? c??ng n???i b???t h???a h???n s??? mang ?????n nh???ng tr???i nghi???m t???t nh???t v??? m???i m???t m?? ch??a m???t chi???c iPhone ti???n nhi???m n??o c?? ???????c.", "iphone", 41990000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/190325/iphone-xr-hopmoi-den-600x600-2-600x600.jpg", "iPhone XR 64GB","L?? chi???c ??i???n tho???i iPhone c?? m???c gi?? d??? ch???u, ph?? h???p v???i nhi???u kh??ch h??ng h??n, iPhone Xr v???n ???????c ??u ??i trang b??? chip Apple A12 m???nh m???, m??n h??nh tai th??? c??ng kh??? n??ng ch???ng n?????c ch???ng b???i.", "iphone", 12190000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/232668/samsung-galaxy-z-fold-2-vang-600x600-600x600.jpg", "Samsung Galaxy Z Fold2 5G","Thu???c d??ng smartphone cao c???p, Samsung Galaxy Z Fold2 5G ???????c Samsung trau chu???t kh??ng ch??? v??? ngo??i sang tr???ng, tinh t??? m?? l???n c??? ???n???i th???t??? b??n trong ?????y m???nh m??? khi???n chi???c smartphone n??y ho??n to??n x???ng ????ng ????? ???????c s??? h???u.", "samsung", 50000000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/210644/iphone-11-128gb-green-600x600.jpg", "iPhone 11 128GB","???????c xem l?? phi??n b???n iPhone \\\"gi?? r???\\\" trong b??? 3 iPhone m???i ra m???t nh??ng iPhone 11 128GB v???n s??? h???u cho m??nh r???t nhi???u ??u ??i???m m?? hi???m c?? m???t chi???c smartphone n??o kh??c s??? h???u. N??ng c???p m???nh m??? v??? c???m camera N??m nay v???i iPhone 11 th?? Apple ???? n??ng c???p kh?? nhi???u v??? camera n???u so s??nh v???i chi???c iPhone Xr 128GB n??m ngo??i.", "iphone", 19490000));
//        listProduct.add(new Product("https://cdn.tgdd.vn/Products/Images/42/210644/iphone-11-128gb-green-600x600.jpg", "","iPhone SE 256GB 2020 cu???i c??ng ???? ???????c Apple ra m???t, v???i ngo???i h??nh nh??? g???n ???????c sao ch??p t??? iPhone 8 nh??ng mang trong m??nh m???t hi???u n??ng m???nh m??? v???i vi x??? l?? A13 Bionic, m???c gi?? h???p d???n h???a h???n s??? l?? y???u t??? thu h??t", "iphone", 19490000));
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("DBProduct");
//        for(Product product : listProduct) {
//            myRef.push().setValue(product);
//        }
        return mView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initItem(){
        rcvProduct = mView.findViewById(R.id.rcv_product);
        viewPagerSlidePhoto = mView.findViewById(R.id.vp_slide_photo);
        circleIndicator = mView.findViewById(R.id.circle_indicator);
        atcProductSearch = mView.findViewById(R.id.atc_product_search);
        nestedScrollViewProduct = mView.findViewById(R.id.scrollViewProduct);
        listSlidePhoto = getListSlidePhoto();
        listAllProduct = new ArrayList<Product>();
        getProducts(listAllProduct);

        nestedScrollViewProduct.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    limitProduct++;
                    // on below line we are making our progress bar visible.
//                    loadingPB.setVisibility(View.VISIBLE);
//                    if (count < 20) {
//                        // on below line we are again calling
//                        // a method to load data in our array list.
//
//                    }
                    getProducts(listAllProduct);
                }
            }
        });

        home = (MainActivity) getActivity();
    }


    private void setDataSlidePhotoAdapter(){
        slidePhotoAdapter = new SlidePhotoAdapter(listSlidePhoto, this);
        viewPagerSlidePhoto.setAdapter(slidePhotoAdapter);
        circleIndicator.setViewPager(viewPagerSlidePhoto);
        slidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImage();
    }

    private void autoSlideImage(){
        if(listSlidePhoto == null || listSlidePhoto.isEmpty() || viewPagerSlidePhoto == null){
            return;
        }
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPagerSlidePhoto.getCurrentItem();
                        int totalItem = listSlidePhoto.size() - 1;

                        // N???u item hi???n t???i ch??a ph???i cu???i c??ng
                        if(currentItem < totalItem){
                            currentItem++;
                            viewPagerSlidePhoto.setCurrentItem(currentItem);
                        }else {
                            viewPagerSlidePhoto.setCurrentItem(0);
                        }
                    }
                });
            }

            // x??? l?? th??m ????? set time
        },500,3000 );
    }

    private void setDataProductAdapter(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(home, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        productAdapter.setData(listAllProduct,home);

        rcvProduct.setAdapter(productAdapter);
    }

    private void setProductSearchAdapter(List<Product> listProduct ){
        ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(home,R.layout.item_search, listProduct);
        atcProductSearch.setAdapter(productSearchAdapter);

        // Sau khi ch???n item search s??? chuy???n sang fragment detail
        atcProductSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                home.toDetailProductFragment(listProduct.get(position));
            }
        });
    }

    private List<SlidePhoto> getListSlidePhoto(){
        List<SlidePhoto> listSlidePhoto = new ArrayList<>();
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide1));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide2));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide3));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide4));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide5));
        return listSlidePhoto;
    }

    private void getProducts(List<Product> products) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBProduct");
        Query productListQuery = myRef.limitToFirst(limitProduct).orderByChild("brand")
                .startAt(startProduct);

        productListQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productAdapter.notifyDataSetChanged();

                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    product.setId(data.getKey());
                    System.out.println(data.getKey());
                    products.add(product);

                }
                setProductSearchAdapter(products);
                startProduct += (limitProduct + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Kh??ng t???i ???????c d??? li???u t??? firebase"
                        +databaseError.toString(),Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled"+ databaseError.toString());
            }


        });

    }


}