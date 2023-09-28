// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAdPaymentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView audiAge;

  @NonNull
  public final TextView balanceWallet;

  @NonNull
  public final CardView cardVie3;

  @NonNull
  public final TextView est;

  @NonNull
  public final TextView gstAmt;

  @NonNull
  public final TextView gstTxt;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView next;

  @NonNull
  public final TextView num;

  @NonNull
  public final RelativeLayout relativeLayout3;

  @NonNull
  public final ScrollView scrollView2;

  @NonNull
  public final ConstraintLayout select;

  @NonNull
  public final TextView servTotal;

  @NonNull
  public final Switch switch1;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView total;

  @NonNull
  public final TextView totalAmt;

  @NonNull
  public final TextView txt;

  @NonNull
  public final TextView walletText;

  private ActivityAdPaymentBinding(@NonNull ConstraintLayout rootView, @NonNull TextView audiAge,
      @NonNull TextView balanceWallet, @NonNull CardView cardVie3, @NonNull TextView est,
      @NonNull TextView gstAmt, @NonNull TextView gstTxt, @NonNull ImageView imageView,
      @NonNull TextView next, @NonNull TextView num, @NonNull RelativeLayout relativeLayout3,
      @NonNull ScrollView scrollView2, @NonNull ConstraintLayout select,
      @NonNull TextView servTotal, @NonNull Switch switch1, @NonNull TextView textView,
      @NonNull TextView total, @NonNull TextView totalAmt, @NonNull TextView txt,
      @NonNull TextView walletText) {
    this.rootView = rootView;
    this.audiAge = audiAge;
    this.balanceWallet = balanceWallet;
    this.cardVie3 = cardVie3;
    this.est = est;
    this.gstAmt = gstAmt;
    this.gstTxt = gstTxt;
    this.imageView = imageView;
    this.next = next;
    this.num = num;
    this.relativeLayout3 = relativeLayout3;
    this.scrollView2 = scrollView2;
    this.select = select;
    this.servTotal = servTotal;
    this.switch1 = switch1;
    this.textView = textView;
    this.total = total;
    this.totalAmt = totalAmt;
    this.txt = txt;
    this.walletText = walletText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAdPaymentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAdPaymentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_ad_payment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAdPaymentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.audi_age;
      TextView audiAge = ViewBindings.findChildViewById(rootView, id);
      if (audiAge == null) {
        break missingId;
      }

      id = R.id.balance_wallet;
      TextView balanceWallet = ViewBindings.findChildViewById(rootView, id);
      if (balanceWallet == null) {
        break missingId;
      }

      id = R.id.cardVie3;
      CardView cardVie3 = ViewBindings.findChildViewById(rootView, id);
      if (cardVie3 == null) {
        break missingId;
      }

      id = R.id.est;
      TextView est = ViewBindings.findChildViewById(rootView, id);
      if (est == null) {
        break missingId;
      }

      id = R.id.gst_amt;
      TextView gstAmt = ViewBindings.findChildViewById(rootView, id);
      if (gstAmt == null) {
        break missingId;
      }

      id = R.id.gst_txt;
      TextView gstTxt = ViewBindings.findChildViewById(rootView, id);
      if (gstTxt == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.next;
      TextView next = ViewBindings.findChildViewById(rootView, id);
      if (next == null) {
        break missingId;
      }

      id = R.id.num;
      TextView num = ViewBindings.findChildViewById(rootView, id);
      if (num == null) {
        break missingId;
      }

      id = R.id.relativeLayout3;
      RelativeLayout relativeLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayout3 == null) {
        break missingId;
      }

      id = R.id.scrollView2;
      ScrollView scrollView2 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView2 == null) {
        break missingId;
      }

      id = R.id.select;
      ConstraintLayout select = ViewBindings.findChildViewById(rootView, id);
      if (select == null) {
        break missingId;
      }

      id = R.id.serv_total;
      TextView servTotal = ViewBindings.findChildViewById(rootView, id);
      if (servTotal == null) {
        break missingId;
      }

      id = R.id.switch_1;
      Switch switch1 = ViewBindings.findChildViewById(rootView, id);
      if (switch1 == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.total;
      TextView total = ViewBindings.findChildViewById(rootView, id);
      if (total == null) {
        break missingId;
      }

      id = R.id.total_amt;
      TextView totalAmt = ViewBindings.findChildViewById(rootView, id);
      if (totalAmt == null) {
        break missingId;
      }

      id = R.id.txt;
      TextView txt = ViewBindings.findChildViewById(rootView, id);
      if (txt == null) {
        break missingId;
      }

      id = R.id.wallet_text;
      TextView walletText = ViewBindings.findChildViewById(rootView, id);
      if (walletText == null) {
        break missingId;
      }

      return new ActivityAdPaymentBinding((ConstraintLayout) rootView, audiAge, balanceWallet,
          cardVie3, est, gstAmt, gstTxt, imageView, next, num, relativeLayout3, scrollView2, select,
          servTotal, switch1, textView, total, totalAmt, txt, walletText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
