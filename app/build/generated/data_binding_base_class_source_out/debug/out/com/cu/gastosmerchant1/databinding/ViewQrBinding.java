// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public final class ViewQrBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView copy;

  @NonNull
  public final TextView delete;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final LinearLayout linearLayout7;

  @NonNull
  public final ImageView qrImg;

  @NonNull
  public final Switch switch1;

  @NonNull
  public final TextView textView10;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final View view;

  @NonNull
  public final TextView wifi;

  private ViewQrBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView copy,
      @NonNull TextView delete, @NonNull ImageView imageView, @NonNull LinearLayout linearLayout7,
      @NonNull ImageView qrImg, @NonNull Switch switch1, @NonNull TextView textView10,
      @NonNull TextView textView3, @NonNull TextView textView4, @NonNull View view,
      @NonNull TextView wifi) {
    this.rootView = rootView;
    this.copy = copy;
    this.delete = delete;
    this.imageView = imageView;
    this.linearLayout7 = linearLayout7;
    this.qrImg = qrImg;
    this.switch1 = switch1;
    this.textView10 = textView10;
    this.textView3 = textView3;
    this.textView4 = textView4;
    this.view = view;
    this.wifi = wifi;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewQrBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewQrBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_qr, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewQrBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.copy;
      ImageView copy = ViewBindings.findChildViewById(rootView, id);
      if (copy == null) {
        break missingId;
      }

      id = R.id.delete;
      TextView delete = ViewBindings.findChildViewById(rootView, id);
      if (delete == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.linearLayout7;
      LinearLayout linearLayout7 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout7 == null) {
        break missingId;
      }

      id = R.id.qr_img;
      ImageView qrImg = ViewBindings.findChildViewById(rootView, id);
      if (qrImg == null) {
        break missingId;
      }

      id = R.id.switch1;
      Switch switch1 = ViewBindings.findChildViewById(rootView, id);
      if (switch1 == null) {
        break missingId;
      }

      id = R.id.textView10;
      TextView textView10 = ViewBindings.findChildViewById(rootView, id);
      if (textView10 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      id = R.id.wifi;
      TextView wifi = ViewBindings.findChildViewById(rootView, id);
      if (wifi == null) {
        break missingId;
      }

      return new ViewQrBinding((ConstraintLayout) rootView, copy, delete, imageView, linearLayout7,
          qrImg, switch1, textView10, textView3, textView4, view, wifi);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
