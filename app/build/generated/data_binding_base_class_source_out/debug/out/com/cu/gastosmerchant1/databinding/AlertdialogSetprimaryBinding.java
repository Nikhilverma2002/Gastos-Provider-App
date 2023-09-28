// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AlertdialogSetprimaryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView qrItemImage;

  @NonNull
  public final CardView setPrimary;

  private AlertdialogSetprimaryBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView qrItemImage, @NonNull CardView setPrimary) {
    this.rootView = rootView;
    this.qrItemImage = qrItemImage;
    this.setPrimary = setPrimary;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AlertdialogSetprimaryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AlertdialogSetprimaryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.alertdialog_setprimary, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AlertdialogSetprimaryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.qrItemImage;
      ImageView qrItemImage = ViewBindings.findChildViewById(rootView, id);
      if (qrItemImage == null) {
        break missingId;
      }

      id = R.id.set_primary;
      CardView setPrimary = ViewBindings.findChildViewById(rootView, id);
      if (setPrimary == null) {
        break missingId;
      }

      return new AlertdialogSetprimaryBinding((LinearLayout) rootView, qrItemImage, setPrimary);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
