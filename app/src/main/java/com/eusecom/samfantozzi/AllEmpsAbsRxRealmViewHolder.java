package com.eusecom.samfantozzi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samfantozzi.models.Employee;

/**
 * The view holder for an item
 */
public class AllEmpsAbsRxRealmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

  public TextView employee_name;
  public ImageView employee_photo;
  public ImageView starView;
  public TextView numStarsView;
  public TextView oscx;
  public TextView icox;
  public TextView typx;
  public TextView emailx;

  private ClickListener clickListener;
  Context mContext;


  public AllEmpsAbsRxRealmViewHolder(View itemView) {
    super(itemView);

    employee_name = (TextView) itemView.findViewById(R.id.employee_name);
    employee_photo = (ImageView) itemView.findViewById(R.id.employee_photo);
    starView = (ImageView) itemView.findViewById(R.id.star);
    numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
    oscx = (TextView) itemView.findViewById(R.id.oscx);
    icox = (TextView) itemView.findViewById(R.id.icox);
    typx = (TextView) itemView.findViewById(R.id.typx);
    emailx = (TextView) itemView.findViewById(R.id.emailx);
    mContext = itemView.getContext();


    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  public void bindModel(Employee employee, String ume) {
    if (employee == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    employee_name.setText(employee.getUsername());
    oscx.setText(employee.getUsosc());
    icox.setText(employee.getUsico());
    typx.setText(employee.getUstype());
    emailx.setText(employee.getEmail());

  }

  /* Interface for handling clicks - both normal and long ones. */
  public interface ClickListener {

    /**
     * Called when the view is clicked.
     *
     * @param v view that is clicked
     * @param position of the clicked item
     * @param isLongClick true if long click, false otherwise
     */
    public void onClick(View v, int position, boolean isLongClick);

  }

  /* Setter for listener. */
  public void setClickListener(ClickListener clickListener) {
    this.clickListener = clickListener;
  }

  @Override
  public void onClick(View v) {

    // If not long clicked, pass last variable as false.
    clickListener.onClick(v, getPosition(), false);
  }

  @Override
  public boolean onLongClick(View v) {

    // If long clicked, passed last variable as true.
    clickListener.onClick(v, getPosition(), true);
    return true;
  }


}
