package com.braintreepayments.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static com.braintreepayments.api.models.BinData.BIN_DATA_KEY;

/**
 * {@link PaymentMethodNonce} representing a credit or debit card.
 */
public class CardNonce extends PaymentMethodNonce implements Parcelable {

    protected static final String TYPE = "CreditCard";
    protected static final String API_RESOURCE_KEY = "creditCards";

    private static final String THREE_D_SECURE_INFO_KEY = "threeDSecureInfo";
    private static final String CARD_DETAILS_KEY = "details";
    private static final String CARD_TYPE_KEY = "cardType";
    private static final String LAST_TWO_KEY = "lastTwo";

    private String mCardType;
    private String mLastTwo;
    private ThreeDSecureInfo mThreeDSecureInfo;
    private BinData mBinData;

    /**
     * Convert an API response to a {@link CardNonce}.
     *
     * @param json Raw JSON response from Braintree of a {@link CardNonce}.
     * @return {@link CardNonce}.
     * @throws JSONException when parsing the response fails.
     */
    public static CardNonce fromJson(String json) throws JSONException {
        CardNonce cardNonce = new CardNonce();
        cardNonce.fromJson(CardNonce.getJsonObjectForType(API_RESOURCE_KEY, json));
        return cardNonce;
    }

    /**
     * Populate properties with values from a {@link JSONObject} .
     *
     * @param json {@link JSONObject}
     * @throws JSONException when parsing fails.
     */
    protected void fromJson(JSONObject json) throws JSONException {
        super.fromJson(json);

        JSONObject details = json.getJSONObject(CARD_DETAILS_KEY);
        mLastTwo = details.getString(LAST_TWO_KEY);
        mCardType = details.getString(CARD_TYPE_KEY);
        mThreeDSecureInfo =
                ThreeDSecureInfo.fromJson(json.optJSONObject(THREE_D_SECURE_INFO_KEY));
        mBinData = BinData.fromJson(json.optJSONObject(BIN_DATA_KEY));
    }

    /**
     * @return Type of this card (e.g. MasterCard, American Express)
     */
    @Override
    public String getTypeLabel() {
        return mCardType;
    }

    /**
     * @return Type of this card (e.g. Visa, MasterCard, American Express)
     */
    public String getCardType() {
        return mCardType;
    }

    /**
     * @return Last two digits of the card, intended for display purposes.
     */
    public String getLastTwo() {
        return mLastTwo;
    }

    /**
     * @return The 3D Secure info for the current {@link CardNonce} or
     * {@code null}
     */
    public ThreeDSecureInfo getThreeDSecureInfo() {
        return mThreeDSecureInfo;
    }

    /**
     * @return The BIN data for the card number associated with {@link CardNonce}
     */
    public BinData getBinData() {
        return mBinData;
    }

    public CardNonce() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCardType);
        dest.writeString(mLastTwo);
        dest.writeParcelable(mBinData, flags);
        dest.writeParcelable(mThreeDSecureInfo, flags);
    }

    protected CardNonce(Parcel in) {
        super(in);
        mCardType = in.readString();
        mLastTwo = in.readString();
        mBinData = in.readParcelable(BinData.class.getClassLoader());
        mThreeDSecureInfo = in.readParcelable(ThreeDSecureInfo.class.getClassLoader());
    }

    public static final Creator<CardNonce> CREATOR = new Creator<CardNonce>() {
        public CardNonce createFromParcel(Parcel source) {
            return new CardNonce(source);
        }

        public CardNonce[] newArray(int size) {
            return new CardNonce[size];
        }
    };
}