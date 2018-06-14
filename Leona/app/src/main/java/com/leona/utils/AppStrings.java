package com.leona.utils;

/**
 * Created by krify on 12/12/16.
 */

public class AppStrings {


    public interface conFig{

         String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
         String REGISTRATION_COMPLETE = "registrationComplete";
         String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
         int NOTIFICATION_ID = 100;
         int NOTIFICATION_ID_BIG_IMAGE = 101;

         String SHARED_PREF = "ah_firebase";
    }

    public interface EmailValidation {

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    }


    public interface constants {

        String devicetype = "1";
        String language_enlish = "1";
        String language_spanish = "0";
        String categoryID = "1";


        String pref_checked = "1";
        String pref_unchecked = "0";
        String min = "0";
        String max = "10";
        String home = "Home";
        String pref= "Pref";
        String nearby = "Nearby";
        String restaurents = "Restaurents";

    }

    public interface symbols{

        String dollar ="$ ";
    }
 public interface intentData {

        String fb_api_response = "fb_api_response";


    }
public interface sortTypes {

        String fb_api_response = "fb_api_response";


    }


    public interface PermissionCode {
        int Storage = 1;
        int Contacts = 2;
        int Location = 3;
    }

public interface From {
    String Discoint = "userID";

}

    public interface RequestedData {


        String userID = "userID";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        String password = "password";
        String deviceID = "deviceID";
        String language = "language";
        String deviceType = "deviceType";
        String latitude = "latitude";
        String longitude = "longitude";


        String facebookID = "facebookID";
        String categoryID = "categoryID";
        String preferenceID = "preferenceID";
        String distance = "distance";
        String discountID = "discountID";
        String min = "min";
        String max = "max";
        String featureType = "featureType";
        String status = "status";

        String merchantID = "merchantID";
        String keyword = "keyword";
        String From = "From";
        String type = "type";


        String profilePic = "profilePic";
        String location = "location";

        String adID = "adID";
        String datetime = "datetime";
    }

    public interface FacebookTags {
        String fb_publicprofile = "public_profile";
        String fb_emailreadper = "email";
        String user_friends = "user_friends";

        String fb_id = "id";
        String fb_name = "name";
        String fb_email = "email";
        String fb_gender = "gender";
        String is_verified = "is_verified";
        String fb_profilepic_link = "https://graph.facebook.com/";
        String fb_profilepic_type = "/picture?type=large";

        String SOCIAL_FACEBOOK_ID = "0";

    }

    public interface Responsedata {


        String userDetails = "userDetails";
        String status = "status";
        String message = "message";

        String facebookID = "facebookID";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        String password = "password";
        String loginType = "loginType";
        String userID = "userID";
        String token = "token";
        String profilePic = "profilePic";
        String location = "location";
        String branchsCount = "branchsCount";
        String latlng = "latlng";
        String branchTime = "branchTime";


        String preferences = "preferences";
        String preferenceID = "preferenceID";
        String preferenceName = "preferenceName";
        String userPreference = "userPreference";
        String categoryID = "categoryID";
        String imageBaseURL = "imageBaseURL";
        String discounts = "discounts";
        String discountID = "discountID";
        String discountName = "discountName";
        String image = "image";
        String adimage = "adimage";
        String originalPrice = "originalPrice";
        String discountPrice = "discountPrice";
        String startTimeDate = "startTimeDate";
        String endTimeDate = "endTimeDate";
        String description = "description";
        String featured = "featured";
        String timeDateStamp = "timeDateStamp";
        String merchantID = "merchantID";
        String Advertisements = "Advertisements";
        String Restaurants = "Restaurants";
        String storeName = "storeName";
        String storeImage = "storeImage";
        String latitude = "latitude";
        String longitude = "longitude";
        String distance = "distance";
        String offersCount = "offersCount";
        String adID = "adID";
        String FavouriteStatus = "FavouriteStatus";
        String favouriteStatus = "favouriteStatus";
        String startTime = "startTime";
        String endTime = "endTime";
        String storeTime = "storeTime";
        String address = "address";


        String question ="question";
        String answer="answer";
        String totalCount ="totalCount";
        String from ="from";
        String notification ="notification";


        String creatDatetime="creatDatetime";
        java.lang.String phoneNum="phoneNum";
        java.lang.String HomeAddress="HomeAddress";
        java.lang.String adressID="adressID";
        java.lang.String pic="pic";
    }

   public interface uploadPic {
        String type_from = "type_from";
        String camera = "camera";
        String gallery = "gallery";
        String camera_video = "camera_video";
        String selected_image_path = "selected_image_path";
        String selected_video_thumbnail_path = "selected_video_thumbnail_path";
        String selected_video_path = "selected_video_path";
    }
}
