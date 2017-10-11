package com.eusecom.samfantozzi;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.rxbus.RxBus;
import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link //BlogPostEntity}
 */
class AllEmpsAbsRxRealmAdapter extends RecyclerView.Adapter<AllEmpsAbsRxRealmViewHolder> {

  private List<Employee> mBlogPostEntities;
  private String umex;
  private RxBus _rxBus;

  public AllEmpsAbsRxRealmAdapter(List<Employee> blogPostEntities, RxBus bus, String ume) {
    mBlogPostEntities = blogPostEntities;
    umex = ume;
    _rxBus = bus;
    //do not work _rxBus = AttendanceApplication.getInstance().getRxBusSingleton();
  }

  @Override public AllEmpsAbsRxRealmViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allempsabs_item, viewGroup, false);
    return new AllEmpsAbsRxRealmViewHolder(view);
  }

  @Override public void onBindViewHolder(AllEmpsAbsRxRealmViewHolder holder, int position) {
    Employee blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity, umex);

    holder.setClickListener(new AllEmpsAbsRxRealmViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = blogPostEntity.getKeyf();
        if (isLongClick) {

          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
          }

        } else {

          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
            Log.d("short click ", blogPostEntity.getUsername() + "");
          }

        }
      }
    });

  }

  @Override public int getItemCount() {
    //Log.d("getItemCount", mBlogPostEntities.size() + "");
    return mBlogPostEntities.size();
  }


  public void remove(int position) {
    mBlogPostEntities.remove(position);
    notifyItemRemoved(position);
  }



  public void add(Employee addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Employee}
   */
  public void setRealmData(List<Employee> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  public Employee getItemAtPosition(int position) {
    return mBlogPostEntities.get(position);
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Employee}
   */
  public void validateData(List<Employee> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
