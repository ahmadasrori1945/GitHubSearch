package id.co.telkomsigma.githubsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {

    private UserModel userModel;
    private List<UserModel> listUser;
    private List<UserModel> listUserFiltered;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public UserAdapter(List<UserModel> listUser,Context context) {
        this.listUser=listUser;
        this.listUserFiltered=listUser;
        mInflater=LayoutInflater.from(context);
        getFilter();
    }
    @Override
    public int getCount() {
        return listUser.size();
    }
    @Override
    public Object getItem(int position) {

        return listUser.get(position);
    }
    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder viewHolder;
        if(convertView==null) {
            viewHolder=new Holder();
            convertView=mInflater.inflate(R.layout.adapter_view_layout,null);
            viewHolder.nameTv=convertView.findViewById(R.id.txt_listitem);
            viewHolder.ivProfile=convertView.findViewById(R.id.ivProfile);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(Holder)convertView.getTag();
        }
        viewHolder.nameTv.setText(listUser.get(position).getLogin()+" : "+position+"/"+listUser.size());
        Picasso.get().load(listUser.get(position).getAvatar()).into(viewHolder.ivProfile);
        return convertView;
    }

    private class  Holder{
        TextView nameTv;
        ImageView ivProfile;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                List<UserModel> filterList = null;
                for(int i=0;i<listUserFiltered.size();i++){
                    if(listUserFiltered.get(i).getLogin().contains(constraint)) {
                        userModel = new UserModel(listUserFiltered.get(i).getLogin(), listUserFiltered.get(i).getAvatar());
                        filterList.add(userModel);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=listUserFiltered.size();
                results.values=listUserFiltered;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            listUser=(List<UserModel>) results.values;
            notifyDataSetChanged();
        }
    }
}