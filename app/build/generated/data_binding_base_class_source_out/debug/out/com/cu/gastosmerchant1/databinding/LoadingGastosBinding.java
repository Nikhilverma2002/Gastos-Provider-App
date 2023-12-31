// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LoadingGastosBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView addDiscount;

  @NonNull
  public final LottieAnimationView animationView;

  @NonNull
  public final ImageView imageView1;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  private LoadingGastosBinding(@NonNull ConstraintLayout rootView, @NonNull TextView addDiscount,
      @NonNull LottieAnimationView animationView, @NonNull ImageView imageView1,
      @NonNull TextView textView3, @NonNull TextView textView4) {
    this.rootView = rootView;
    this.addDiscount = addDiscount;
    this.animationView = animationView;
    this.imageView1 = imageView1;
    this.textView3 = textView3;
    this.textView4 = textView4;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LoadingGastosBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LoadingGastosBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.loading_gastos, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LoadingGastosBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.add_discount;
      TextView addDiscount = ViewBindings.findChildViewById(rootView, id);
      if (addDiscount == null) {
        break missingId;
      }

      id = R.id.animation_view;
      LottieAnimationView animationView = ViewBindings.findChildViewById(rootView, id);
      if (animationView == null) {
        break missingId;
      }

      id = R.id.imageView1;
      ImageView imageView1 = ViewBindings.findChildViewById(rootView, id);
      if (imageView1 == null) {
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

      return new LoadingGastosBinding((ConstraintLayout) rootView, addDiscount, animationView,
          imageView1, textView3, textView4);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
