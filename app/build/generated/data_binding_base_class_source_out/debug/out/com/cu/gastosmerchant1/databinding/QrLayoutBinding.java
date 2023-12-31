// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class QrLayoutBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView bharatpay;

  @NonNull
  public final LinearLayout linearLayout3;

  @NonNull
  public final TextView primary;

  @NonNull
  public final TextView textView10;

  @NonNull
  public final TextView wifi;

  private QrLayoutBinding(@NonNull ConstraintLayout rootView, @NonNull TextView bharatpay,
      @NonNull LinearLayout linearLayout3, @NonNull TextView primary, @NonNull TextView textView10,
      @NonNull TextView wifi) {
    this.rootView = rootView;
    this.bharatpay = bharatpay;
    this.linearLayout3 = linearLayout3;
    this.primary = primary;
    this.textView10 = textView10;
    this.wifi = wifi;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static QrLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static QrLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.qr_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static QrLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bharatpay;
      TextView bharatpay = ViewBindings.findChildViewById(rootView, id);
      if (bharatpay == null) {
        break missingId;
      }

      id = R.id.linearLayout3;
      LinearLayout linearLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout3 == null) {
        break missingId;
      }

      id = R.id.primary;
      TextView primary = ViewBindings.findChildViewById(rootView, id);
      if (primary == null) {
        break missingId;
      }

      id = R.id.textView10;
      TextView textView10 = ViewBindings.findChildViewById(rootView, id);
      if (textView10 == null) {
        break missingId;
      }

      id = R.id.wifi;
      TextView wifi = ViewBindings.findChildViewById(rootView, id);
      if (wifi == null) {
        break missingId;
      }

      return new QrLayoutBinding((ConstraintLayout) rootView, bharatpay, linearLayout3, primary,
          textView10, wifi);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
