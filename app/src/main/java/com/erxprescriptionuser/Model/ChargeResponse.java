package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class ChargeResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("live_mode")
    @Expose
    private Boolean liveMode;
    @SerializedName("api_version")
    @Expose
    private String apiVersion;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("threeDSecure")
    @Expose
    private Boolean threeDSecure;
    @SerializedName("card_threeDSecure")
    @Expose
    private Boolean cardThreeDSecure;
    @SerializedName("save_card")
    @Expose
    private Boolean saveCard;
    @SerializedName("statement_descriptor")
    @Expose
    private String statementDescriptor;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("reference")
    @Expose
    private Reference reference;
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("receipt")
    @Expose
    private Receipt receipt;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("redirect")
    @Expose
    private Redirect redirect;
    @SerializedName("post")
    @Expose
    private Post post;

    protected ChargeResponse(Parcel in) {
        id = in.readString();
        object = in.readString();
        byte tmpLiveMode = in.readByte();
        liveMode = tmpLiveMode == 0 ? null : tmpLiveMode == 1;
        apiVersion = in.readString();
        method = in.readString();
        status = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        currency = in.readString();
        byte tmpThreeDSecure = in.readByte();
        threeDSecure = tmpThreeDSecure == 0 ? null : tmpThreeDSecure == 1;
        byte tmpCardThreeDSecure = in.readByte();
        cardThreeDSecure = tmpCardThreeDSecure == 0 ? null : tmpCardThreeDSecure == 1;
        byte tmpSaveCard = in.readByte();
        saveCard = tmpSaveCard == 0 ? null : tmpSaveCard == 1;
        statementDescriptor = in.readString();
        description = in.readString();
        metadata = in.readParcelable(Metadata.class.getClassLoader());
        transaction = in.readParcelable(Transaction.class.getClassLoader());
        reference = in.readParcelable(Reference.class.getClassLoader());
        response = in.readParcelable(Response.class.getClassLoader());
        receipt = in.readParcelable(Receipt.class.getClassLoader());
        customer = in.readParcelable(Customer.class.getClassLoader());
        source = in.readParcelable(Source.class.getClassLoader());
        redirect = in.readParcelable(Redirect.class.getClassLoader());
        post = in.readParcelable(Post.class.getClassLoader());
    }

    public static final Creator<ChargeResponse> CREATOR = new Creator<ChargeResponse>() {
        @Override
        public ChargeResponse createFromParcel(Parcel in) {
            return new ChargeResponse(in);
        }

        @Override
        public ChargeResponse[] newArray(int size) {
            return new ChargeResponse[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Boolean getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(Boolean liveMode) {
        this.liveMode = liveMode;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getThreeDSecure() {
        return threeDSecure;
    }

    public void setThreeDSecure(Boolean threeDSecure) {
        this.threeDSecure = threeDSecure;
    }

    public Boolean getCardThreeDSecure() {
        return cardThreeDSecure;
    }

    public void setCardThreeDSecure(Boolean cardThreeDSecure) {
        this.cardThreeDSecure = cardThreeDSecure;
    }

    public Boolean getSaveCard() {
        return saveCard;
    }

    public void setSaveCard(Boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getStatementDescriptor() {
        return statementDescriptor;
    }

    public void setStatementDescriptor(String statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(object);
        dest.writeByte((byte) (liveMode == null ? 0 : liveMode ? 1 : 2));
        dest.writeString(apiVersion);
        dest.writeString(method);
        dest.writeString(status);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
        dest.writeString(currency);
        dest.writeByte((byte) (threeDSecure == null ? 0 : threeDSecure ? 1 : 2));
        dest.writeByte((byte) (cardThreeDSecure == null ? 0 : cardThreeDSecure ? 1 : 2));
        dest.writeByte((byte) (saveCard == null ? 0 : saveCard ? 1 : 2));
        dest.writeString(statementDescriptor);
        dest.writeString(description);
        dest.writeParcelable(metadata, flags);
        dest.writeParcelable(transaction, flags);
        dest.writeParcelable(reference, flags);
        dest.writeParcelable(response, flags);
        dest.writeParcelable(receipt, flags);
        dest.writeParcelable(customer, flags);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(redirect, flags);
        dest.writeParcelable(post, flags);
    }


  public static class Customer implements Parcelable {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private Phone phone;

      protected Customer(Parcel in) {
          firstName = in.readString();
          email = in.readString();
          phone = in.readParcelable(Phone.class.getClassLoader());
      }

      public static final Creator<Customer> CREATOR = new Creator<Customer>() {
          @Override
          public Customer createFromParcel(Parcel in) {
              return new Customer(in);
          }

          @Override
          public Customer[] newArray(int size) {
              return new Customer[size];
          }
      };

      public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

      @Override
      public int describeContents() {
          return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(firstName);
          dest.writeString(email);
          dest.writeParcelable(phone, flags);
      }
  }


 public static class Expiry implements Parcelable
 {

    @SerializedName("period")
    @Expose
    private Integer period;
    @SerializedName("type")
    @Expose
    private String type;

     protected Expiry(Parcel in) {
         if (in.readByte() == 0) {
             period = null;
         } else {
             period = in.readInt();
         }
         type = in.readString();
     }

     public static final Creator<Expiry> CREATOR = new Creator<Expiry>() {
         @Override
         public Expiry createFromParcel(Parcel in) {
             return new Expiry(in);
         }

         @Override
         public Expiry[] newArray(int size) {
             return new Expiry[size];
         }
     };

     public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         if (period == null) {
             dest.writeByte((byte) 0);
         } else {
             dest.writeByte((byte) 1);
             dest.writeInt(period);
         }
         dest.writeString(type);
     }
 }
  public static class Metadata implements Parcelable{

    @SerializedName("udf1")
    @Expose
    private String udf1;
    @SerializedName("udf2")
    @Expose
    private String udf2;

      protected Metadata(Parcel in) {
          udf1 = in.readString();
          udf2 = in.readString();
      }

      public static final Creator<Metadata> CREATOR = new Creator<Metadata>() {
          @Override
          public Metadata createFromParcel(Parcel in) {
              return new Metadata(in);
          }

          @Override
          public Metadata[] newArray(int size) {
              return new Metadata[size];
          }
      };

      public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

      @Override
      public int describeContents() {
          return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(udf1);
          dest.writeString(udf2);
      }
  }
 public static class Phone implements Parcelable {

    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("number")
    @Expose
    private String number;

     protected Phone(Parcel in) {
         countryCode = in.readString();
         number = in.readString();
     }

     public static final Creator<Phone> CREATOR = new Creator<Phone>() {
         @Override
         public Phone createFromParcel(Parcel in) {
             return new Phone(in);
         }

         @Override
         public Phone[] newArray(int size) {
             return new Phone[size];
         }
     };

     public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(countryCode);
         dest.writeString(number);
     }
 }
 public static class Post implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;

     protected Post(Parcel in) {
         status = in.readString();
         url = in.readString();
     }

     public static final Creator<Post> CREATOR = new Creator<Post>() {
         @Override
         public Post createFromParcel(Parcel in) {
             return new Post(in);
         }

         @Override
         public Post[] newArray(int size) {
             return new Post[size];
         }
     };

     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(status);
         dest.writeString(url);
     }
 }

 public static class Receipt implements Parcelable {

    @SerializedName("email")
    @Expose
    private Boolean email;
    @SerializedName("sms")
    @Expose
    private Boolean sms;

     protected Receipt(Parcel in) {
         byte tmpEmail = in.readByte();
         email = tmpEmail == 0 ? null : tmpEmail == 1;
         byte tmpSms = in.readByte();
         sms = tmpSms == 0 ? null : tmpSms == 1;
     }

     public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
         @Override
         public Receipt createFromParcel(Parcel in) {
             return new Receipt(in);
         }

         @Override
         public Receipt[] newArray(int size) {
             return new Receipt[size];
         }
     };

     public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeByte((byte) (email == null ? 0 : email ? 1 : 2));
         dest.writeByte((byte) (sms == null ? 0 : sms ? 1 : 2));
     }
 }

 public static class Redirect implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;

     protected Redirect(Parcel in) {
         status = in.readString();
         url = in.readString();
     }

     public static final Creator<Redirect> CREATOR = new Creator<Redirect>() {
         @Override
         public Redirect createFromParcel(Parcel in) {
             return new Redirect(in);
         }

         @Override
         public Redirect[] newArray(int size) {
             return new Redirect[size];
         }
     };

     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(status);
         dest.writeString(url);
     }
 }
 public static class Reference implements Parcelable{

    @SerializedName("transaction")
    @Expose
    private String transaction;
    @SerializedName("order")
    @Expose
    private String order;

     protected Reference(Parcel in) {
         transaction = in.readString();
         order = in.readString();
     }

     public static final Creator<Reference> CREATOR = new Creator<Reference>() {
         @Override
         public Reference createFromParcel(Parcel in) {
             return new Reference(in);
         }

         @Override
         public Reference[] newArray(int size) {
             return new Reference[size];
         }
     };

     public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(transaction);
         dest.writeString(order);
     }
 }

 public static class Response implements Parcelable {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

     protected Response(Parcel in) {
         code = in.readString();
         message = in.readString();
     }

     public static final Creator<Response> CREATOR = new Creator<Response>() {
         @Override
         public Response createFromParcel(Parcel in) {
             return new Response(in);
         }

         @Override
         public Response[] newArray(int size) {
             return new Response[size];
         }
     };

     public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(code);
         dest.writeString(message);
     }
 }

 public static class Source implements Parcelable {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("id")
    @Expose
    private String id;

     protected Source(Parcel in) {
         object = in.readString();
         id = in.readString();
     }

     public static final Creator<Source> CREATOR = new Creator<Source>() {
         @Override
         public Source createFromParcel(Parcel in) {
             return new Source(in);
         }

         @Override
         public Source[] newArray(int size) {
             return new Source[size];
         }
     };

     public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(object);
         dest.writeString(id);
     }
 }
  public  static class Transaction implements Parcelable {

      @SerializedName("timezone")
      @Expose
      private String timezone;
      @SerializedName("created")
      @Expose
      private String created;
      @SerializedName("url")
      @Expose
      private String url;
      @SerializedName("expiry")
      @Expose
      private Expiry expiry;
      @SerializedName("asynchronous")
      @Expose
      private Boolean asynchronous;
      @SerializedName("amount")
      @Expose
      private Double amount;
      @SerializedName("currency")
      @Expose
      private String currency;

      protected Transaction(Parcel in) {
          timezone = in.readString();
          created = in.readString();
          url = in.readString();
          byte tmpAsynchronous = in.readByte();
          asynchronous = tmpAsynchronous == 0 ? null : tmpAsynchronous == 1;
          if (in.readByte() == 0) {
              amount = null;
          } else {
              amount = in.readDouble();
          }
          currency = in.readString();
      }

      public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
          @Override
          public Transaction createFromParcel(Parcel in) {
              return new Transaction(in);
          }

          @Override
          public Transaction[] newArray(int size) {
              return new Transaction[size];
          }
      };

      public String getTimezone() {
          return timezone;
      }

      public void setTimezone(String timezone) {
          this.timezone = timezone;
      }

      public String getCreated() {
          return created;
      }

      public void setCreated(String created) {
          this.created = created;
      }

      public String getUrl() {
          return url;
      }

      public void setUrl(String url) {
          this.url = url;
      }

      public Expiry getExpiry() {
          return expiry;
      }

      public void setExpiry(Expiry expiry) {
          this.expiry = expiry;
      }

      public Boolean getAsynchronous() {
          return asynchronous;
      }

      public void setAsynchronous(Boolean asynchronous) {
          this.asynchronous = asynchronous;
      }

      public Double getAmount() {
          return amount;
      }

      public void setAmount(Double amount) {
          this.amount = amount;
      }

      public String getCurrency() {
          return currency;
      }

      public void setCurrency(String currency) {
          this.currency = currency;
      }

      @Override
      public int describeContents() {
          return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(timezone);
          dest.writeString(created);
          dest.writeString(url);
          dest.writeByte((byte) (asynchronous == null ? 0 : asynchronous ? 1 : 2));
          if (amount == null) {
              dest.writeByte((byte) 0);
          } else {
              dest.writeByte((byte) 1);
              dest.writeDouble(amount);
          }
          dest.writeString(currency);
      }
  }
 }