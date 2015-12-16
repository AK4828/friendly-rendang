package id.merv.cdp.book.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import id.merv.cdp.book.MeruvianBookApplication;
import com.meruvian.dnabook.R;
import id.merv.cdp.book.fragment.DownloadedBookFragment;
import id.merv.cdp.book.fragment.FragmentUtils;
import id.merv.cdp.book.holder.TreeViewHolder;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.container_nav_view);

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

        TreeNode root = TreeNode.root();
//        TreeNode computerRoot = new TreeNode("My Computer");
        TreeNode computerRoot = new TreeNode(new TreeViewHolder.TreeviewItem("JENI"));

        TreeNode myDocuments = new TreeNode(new TreeViewHolder.TreeviewItem("JENI 1"));
        TreeNode downloads = new TreeNode(new TreeViewHolder.TreeviewItem("Pemrograman Dasar"));
        TreeNode file1 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        TreeNode file2 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        TreeNode file3 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        TreeNode file4 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        downloads.addChildren(file1, file2, file3, file4);

        TreeNode myMedia = new TreeNode(new TreeViewHolder.TreeviewItem("JENI 2"));
        TreeNode photo1 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        TreeNode photo2 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        TreeNode photo3 = new TreeNode(new TreeViewHolder.TreeviewItem("My Documents"));
        myMedia.addChildren(photo1, photo2, photo3);

        myDocuments.addChild(downloads);
        computerRoot.addChildren(myDocuments, myMedia);

        root.addChildren(computerRoot);

        AndroidTreeView treeView = new AndroidTreeView(this, root);
        treeView.setDefaultAnimation(true);
        treeView.setDefaultViewHolder(TreeViewHolder.class);

        viewGroup.addView(treeView.getView());

//        if (savedInstanceState != null) {
//            String state = savedInstanceState.getString("tState");
//            if (!TextUtils.isEmpty(state)) {
//                treeView.restoreState(state);
//            }
//        }
    }

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
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
