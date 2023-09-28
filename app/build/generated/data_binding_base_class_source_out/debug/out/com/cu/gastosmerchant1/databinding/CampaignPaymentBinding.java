// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

public final class CampaignPaymentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LottieAnimationView animate;

  @NonNull
  public final ConstraintLayout cons1;

  @NonNull
  public final TextView date;

  @NonNull
  public final TextView dateTxt;

  @NonNull
  public final TextView gst;

  @NonNull
  public final EditText guidedby;

  @NonNull
  public final TextView layout1;

  @NonNull
  public final TextView proceedPay;

  @NonNull
  public final ProgressBar progressBar2;

  @NonNull
  public final ScrollView scrollView3;

  @NonNull
  public final TextView serviceCharge;

  @NonNull
  public final TextView textView14;

  @NonNull
  public final TextView textView21;

  @NonNull
  public final TextView textView29;

  @NonNull
  public final TextView textView30;

  @NonNull
  public final TextView textView31;

  @NonNull
  public final TextView textView32;

  @NonNull
  public final TextView textView33;

  @NonNull
  public final TextView textView36;

  @NonNull
  public final TextView time;

  @NonNull
  public final TextView timeTxt;

  @NonNull
  public final ConstraintLayout validateLayout;

  private CampaignPaymentBinding(@NonNull ConstraintLayout rootView,
      @NonNull LottieAnimationView animate, @NonNull ConstraintLayout cons1, @NonNull TextView date,
      @NonNull TextView dateTxt, @NonNull TextView gst, @NonNull EditText guidedby,
      @NonNull TextView layout1, @NonNull TextView proceedPay, @NonNull ProgressBar progressBar2,
      @NonNull ScrollView scrollView3, @NonNull TextView serviceCharge,
      @NonNull TextView textView14, @NonNull TextView textView21, @NonNull TextView textView29,
      @NonNull TextView textView30, @NonNull TextView textView31, @NonNull TextView textView32,
      @NonNull TextView textView33, @NonNull TextView textView36, @NonNull TextView time,
      @NonNull TextView timeTxt, @NonNull ConstraintLayout validateLayout) {
    this.rootView = rootView;
    this.animate = animate;
    this.cons1 = cons1;
    this.date = date;
    this.dateTxt = dateTxt;
    this.gst = gst;
    this.guidedby = guidedby;
    this.layout1 = layout1;
    this.proceedPay = proceedPay;
    this.progressBar2 = progressBar2;
    this.scrollView3 = scrollView3;
    this.serviceCharge = serviceCharge;
    this.textView14 = textView14;
    this.textView21 = textView21;
    this.textView29 = textView29;
    this.textView30 = textView30;
    this.textView31 = textView31;
    this.textView32 = textView32;
    this.textView33 = textView33;
    this.textView36 = textView36;
    this.time = time;
    this.timeTxt = timeTxt;
    this.validateLayout = validateLayout;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CampaignPaymentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CampaignPaymentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.campaign_payment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CampaignPaymentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.animate;
      LottieAnimationView animate = ViewBindings.findChildViewById(rootView, id);
      if (animate == null) {
        break missingId;
      }

      id = R.id.cons1;
      ConstraintLayout cons1 = ViewBindings.findChildViewById(rootView, id);
      if (cons1 == null) {
        break missingId;
      }

      id = R.id.date;
      TextView date = ViewBindings.findChildViewById(rootView, id);
      if (date == null) {
        break missingId;
      }

      id = R.id.date_txt;
      TextView dateTxt = ViewBindings.findChildViewById(rootView, id);
      if (dateTxt == null) {
        break missingId;
      }

      id = R.id.gst;
      TextView gst = ViewBindings.findChildViewById(rootView, id);
      if (gst == null) {
        break missingId;
      }

      id = R.id.guidedby;
      EditText guidedby = ViewBindings.findChildViewById(rootView, id);
      if (guidedby == null) {
        break missingId;
      }

      id = R.id.layout1;
      TextView layout1 = ViewBindings.findChildViewById(rootView, id);
      if (layout1 == null) {
        break missingId;
      }

      id = R.id.proceed_pay;
      TextView proceedPay = ViewBindings.findChildViewById(rootView, id);
      if (proceedPay == null) {
        break missingId;
      }

      id = R.id.progressBar2;
      ProgressBar progressBar2 = ViewBindings.findChildViewById(rootView, id);
      if (progressBar2 == null) {
        break missingId;
      }

      id = R.id.scrollView3;
      ScrollView scrollView3 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView3 == null) {
        break missingId;
      }

      id = R.id.service_charge;
      TextView serviceCharge = ViewBindings.findChildViewById(rootView, id);
      if (serviceCharge == null) {
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

      id = R.id.textView29;
      TextView textView29 = ViewBindings.findChildViewById(rootView, id);
      if (textView29 == null) {
        break missingId;
      }

      id = R.id.textView30;
      TextView textView30 = ViewBindings.findChildViewById(rootView, id);
      if (textView30 == null) {
        break missingId;
      }

      id = R.id.textView31;
      TextView textView31 = ViewBindings.findChildViewById(rootView, id);
      if (textView31 == null) {
        break missingId;
      }

      id = R.id.textView32;
      TextView textView32 = ViewBindings.findChildViewById(rootView, id);
      if (textView32 == null) {
        break missingId;
      }

      id = R.id.textView33;
      TextView textView33 = ViewBindings.findChildViewById(rootView, id);
      if (textView33 == null) {
        break missingId;
      }

      id = R.id.textView36;
      TextView textView36 = ViewBindings.findChildViewById(rootView, id);
      if (textView36 == null) {
        break missingId;
      }

      id = R.id.time;
      TextView time = ViewBindings.findChildViewById(rootView, id);
      if (time == null) {
        break missingId;
      }

      id = R.id.time_txt;
      TextView timeTxt = ViewBindings.findChildViewById(rootView, id);
      if (timeTxt == null) {
        break missingId;
      }

      id = R.id.validate_layout;
      ConstraintLayout validateLayout = ViewBindings.findChildViewById(rootView, id);
      if (validateLayout == null) {
        break missingId;
      }

      return new CampaignPaymentBinding((ConstraintLayout) rootView, animate, cons1, date, dateTxt,
          gst, guidedby, layout1, proceedPay, progressBar2, scrollView3, serviceCharge, textView14,
          textView21, textView29, textView30, textView31, textView32, textView33, textView36, time,
          timeTxt, validateLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
