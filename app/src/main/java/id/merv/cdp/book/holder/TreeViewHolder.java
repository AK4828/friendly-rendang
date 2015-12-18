package id.merv.cdp.book.holder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.meruvian.dnabook.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by akm on 14/12/15.
 */
public class TreeViewHolder extends TreeNode.BaseNodeViewHolder<TreeViewHolder.TreeviewItem> {

    private TextView tvValue;
    private PrintView arrowView;

    public TreeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, TreeviewItem value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_tree, null, false);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }


    public static class TreeviewItem {
        public String text;

        public TreeviewItem(String text) {
            this.text = text;

        }

    }
}
