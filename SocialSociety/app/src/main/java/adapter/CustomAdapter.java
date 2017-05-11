package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.ItemObject;
import com.inkswipe.SocialSociety.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<ItemObject> listStorage;

    public CustomAdapter(Context context, List<ItemObject> customizedListView) {
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.listview_with_text_image, parent, false);

            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.textView);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.imageView);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(position==0 || position==3 || position==6 || position==8)
            {
                lp.setMargins(30, 0, 0, 0);
                listViewHolder.imageInListView.setLayoutParams(lp);
                listViewHolder.imageInListView.getLayoutParams().height = 40;
                listViewHolder.imageInListView.getLayoutParams().width=40;
                listViewHolder.textInListView.setTextSize(16);
            }else if(position==1 || position==2 || position==4 || position==5 || position==7 || position==9 || position==10)
            {
                lp.setMargins(110, 0, 0, 0);
                listViewHolder.imageInListView.setLayoutParams(lp);
                listViewHolder.imageInListView.getLayoutParams().height = 32;
                listViewHolder.imageInListView.getLayoutParams().width=32;
            }
            else
            {
                lp.setMargins(30, 0, 0, 0);
                listViewHolder.imageInListView.setLayoutParams(lp);
                listViewHolder.imageInListView.getLayoutParams().height = 40;
                listViewHolder.imageInListView.getLayoutParams().width=40;
                listViewHolder.textInListView.setTextSize(16);
            }

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageResource(listStorage.get(position).getImageId());

        return convertView;
    }

    static class ViewHolder {

        TextView textInListView;
        ImageView imageInListView;
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}