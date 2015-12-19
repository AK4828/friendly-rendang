package id.merv.cdp.book.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import id.merv.cdp.book.MeruvianBookApplication;

import com.github.johnkil.print.PrintView;
import com.meruvian.dnabook.R;

import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.fragment.ChoosedBookCategoryFragment;
import id.merv.cdp.book.fragment.ChoosedCategoryBookDetailFragment;
import id.merv.cdp.book.fragment.DownloadedBookFragment;
import id.merv.cdp.book.fragment.FragmentUtils;
import id.merv.cdp.book.holder.TreeViewHolder;
import id.merv.cdp.book.service.CategoriesService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.path.android.jobqueue.JobManager;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MainBody<Categories> mainBody;
    private ViewGroup viewGroup;
    private CategoriesService service;
    private PrintView arrowView;
    private JobManager jobManager;
    private String getCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        Retrofit retrofit = MeruvianBookApplication.getInstance().getRetrofit();
        service = retrofit.create(CategoriesService.class);
        viewGroup = (ViewGroup) findViewById(R.id.container_nav_view);
        arrowView = (PrintView) findViewById(R.id.arrow_icon);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        if (savedInstanceState == null) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), DownloadedBookFragment.newInstance(), false);
        }

        try {
            Call<MainBody<Categories>> getParentCategories = service.getParentNameCategory();

            getParentCategories.enqueue(new Callback<MainBody<Categories>>() {
                @Override
                public void onResponse(Response<MainBody<Categories>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        mainBody = response.body();
                        final List<Categories> categoryNames = mainBody.getContent();
                        for (Categories c : categoryNames) {
                            TreeNode root = TreeNode.root();
                            final TreeNode categoriesRoot = new TreeNode(new TreeViewHolder.TreeviewItem(c.getName(), c.getId()));
                            root.addChildren(categoriesRoot);

                            getSubCategories(c.getId(), categoriesRoot);

                            AndroidTreeView treeView = new AndroidTreeView(MainActivity.this, root);
                            treeView.setDefaultAnimation(true);
                            treeView.setDefaultViewHolder(TreeViewHolder.class);
                            treeView.setDefaultNodeClickListener(nodeClickListener);

                            viewGroup.addView(treeView.getView());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Error", t.getMessage());

                }
            });

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }

    private void getSubCategories(String parentId, final TreeNode parentNode) {
        Call<MainBody<Categories>> getChildCategories = service.getChildNameCategories(parentId);
        getChildCategories.enqueue(new Callback<MainBody<Categories>>() {
            @Override
            public void onResponse(Response<MainBody<Categories>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    mainBody = response.body();

                    if (mainBody.getTotalElements() < 1) {
//                        parentNode.set
                    }

                    for (Categories c : mainBody.getContent()) {
                        TreeNode categoriesChild = new TreeNode(new TreeViewHolder.TreeviewItem(c.getName(), c.getId()));
                        parentNode.addChildren(categoriesChild);
                        if (c.getId().isEmpty()) {
                            arrowView.setIconText(R.string.ic_drive_file);
                        }
                        getSubCategories(c.getId(), categoriesChild);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            TreeViewHolder.TreeviewItem i = (TreeViewHolder.TreeviewItem) node.getValue();
            FragmentUtils.replaceFragment(getSupportFragmentManager(), ChoosedBookCategoryFragment.newInstance(i.value), false);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        if (getResources().getString(R.string.logout).equals(item.getTitle())) {
//            logout();
        }

        if (getResources().getString(R.string.home).equals(item.getTitle())) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentUtils.replaceFragment(getSupportFragmentManager(), DownloadedBookFragment.newInstance(), false);
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                finish();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void getFragment(String id, String attachmentsId) {
        Log.d("tes","sdsdsds");
        FragmentUtils.replaceFragment(getSupportFragmentManager(), ChoosedCategoryBookDetailFragment.newInstance(id, attachmentsId), false);
    }
}
