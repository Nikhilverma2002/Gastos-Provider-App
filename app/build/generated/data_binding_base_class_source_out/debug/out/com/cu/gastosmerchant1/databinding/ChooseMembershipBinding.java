// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public final class ChooseMembershipBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView check;

  @NonNull
  public final ImageView check1;

  @NonNull
  public final ConstraintLayout cons1;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final ImageView continueBtn;

  @NonNull
  public final ImageView imageView12;

  @NonNull
  public final ImageView imageView13;

  @NonNull
  public final ImageView imageView131;

  @NonNull
  public final ImageView imageView14;

  @NonNull
  public final ImageView imageView141;

  @NonNull
  public final ScrollView scrollView3;

  @NonNull
  public final TextView textView14;

  @NonNull
  public final TextView textView21;

  @NonNull
  public final TextView textView26;

  @NonNull
  public final TextView textView27;

  @NonNull
  public final TextView textView271;

  @NonNull
  public final TextView textView28;

  @NonNull
  public final TextView textView281;

  private ChooseMembershipBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView check,
      @NonNull ImageView check1, @NonNull ConstraintLayout cons1,
      @NonNull ConstraintLayout constraintLayout, @NonNull ImageView continueBtn,
      @NonNull ImageView imageView12, @NonNull ImageView imageView13,
      @NonNull ImageView imageView131, @NonNull ImageView imageView14,
      @NonNull ImageView imageView141, @NonNull ScrollView scrollView3,
      @NonNull TextView textView14, @NonNull TextView textView21, @NonNull TextView textView26,
      @NonNull TextView textView27, @NonNull TextView textView271, @NonNull TextView textView28,
      @NonNull TextView textView281) {
    this.rootView = rootView;
    this.check = check;
    this.check1 = check1;
    this.cons1 = cons1;
    this.constraintLayout = constraintLayout;
    this.continueBtn = continueBtn;
    this.imageView12 = imageView12;
    this.imageView13 = imageView13;
    this.imageView131 = imageView131;
    this.imageView14 = imageView14;
    this.imageView141 = imageView141;
    this.scrollView3 = scrollView3;
    this.textView14 = textView14;
    this.textView21 = textView21;
    this.textView26 = textView26;
    this.textView27 = textView27;
    this.textView271 = textView271;
    this.textView28 = textView28;
    this.textView281 = textView281;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ChooseMembershipBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ChooseMembershipBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.choose_membership, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ChooseMembershipBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.check;
      ImageView check = ViewBindings.findChildViewById(rootView, id);
      if (check == null) {
        break missingId;
      }

      id = R.id.check1;
      ImageView check1 = ViewBindings.findChildViewById(rootView, id);
      if (check1 == null) {
        break missingId;
      }

      id = R.id.cons1;
      ConstraintLayout cons1 = ViewBindings.findChildViewById(rootView, id);
      if (cons1 == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.continue_btn;
      ImageView continueBtn = ViewBindings.findChildViewById(rootView, id);
      if (continueBtn == null) {
        break missingId;
      }

      id = R.id.imageView12;
      ImageView imageView12 = ViewBindings.findChildViewById(rootView, id);
      if (imageView12 == null) {
        break missingId;
      }

      id = R.id.imageView13;
      ImageView imageView13 = ViewBindings.findChildViewById(rootView, id);
      if (imageView13 == null) {
        break missingId;
      }

      id = R.id.imageView131;
      ImageView imageView131 = ViewBindings.findChildViewById(rootView, id);
      if (imageView131 == null) {
        break missingId;
      }

      id = R.id.imageView14;
      ImageView imageView14 = ViewBindings.findChildViewById(rootView, id);
      if (imageView14 == null) {
        break missingId;
      }

      id = R.id.imageView141;
      ImageView imageView141 = ViewBindings.findChildViewById(rootView, id);
      if (imageView141 == null) {
        break missingId;
      }

      id = R.id.scrollView3;
      ScrollView scrollView3 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView3 == null) {
        break missingId;
      }

      id = R.id.textView14;
      TextView textView14 = ViewBindings.findChildViewById(rootView, id);
      if (textView14 == null) {
        break missingId;
      }

      id = R.id.textView21;
      TextView textView21 = ViewBindings.findChildViewById(rootView, id);
      if (textView21 == null) {
        break missingId;
      }

      id = R.id.textView26;
      TextView textView26 = ViewBindings.findChildViewById(rootView, id);
      if (textView26 == null) {
        break missingId;
      }

      id = R.id.textView27;
      TextView textView27 = ViewBindings.findChildViewById(rootView, id);
      if (textView27 == null) {
        break missingId;
      }

      id = R.id.textView271;
      TextView textView271 = ViewBindings.findChildViewById(rootView, id);
      if (textView271 == null) {
        break missingId;
      }

      id = R.id.textView28;
      TextView textView28 = ViewBindings.findChildViewById(rootView, id);
      if (textView28 == null) {
        break missingId;
      }

      id = R.id.textView281;
      TextView textView281 = ViewBindings.findChildViewById(rootView, id);
      if (textView281 == null) {
        break missingId;
      }

      return new ChooseMembershipBinding((ConstraintLayout) rootView, check, check1, cons1,
          constraintLayout, continueBtn, imageView12, imageView13, imageView131, imageView14,
          imageView141, scrollView3, textView14, textView21, textView26, textView27, textView271,
          textView28, textView281);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}