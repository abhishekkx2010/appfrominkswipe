package model;

import com.inkswipe.SocialSociety.ItemObject;
import com.inkswipe.SocialSociety.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by preet on 9/23/2016.
 */
public class DrawerList {

    public static List<ItemObject> drawer(){

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Profile", R.mipmap.drawer_profile));
        listViewItems.add(new ItemObject("Add Society", R.mipmap.drawer_addsociety));
        listViewItems.add(new ItemObject("My Properties", R.mipmap.drawer_property));
        listViewItems.add(new ItemObject("Events", R.mipmap.drawer_event));
        listViewItems.add(new ItemObject("Create Event", R.mipmap.drawer_createevent));
        listViewItems.add(new ItemObject("Archived Events", R.mipmap.drawer_archive));
        listViewItems.add(new ItemObject("Announcements", R.mipmap.drawer_announcement));
        listViewItems.add(new ItemObject("Create Announcement", R.mipmap.drawer_addsociety));
        listViewItems.add(new ItemObject("Polls", R.mipmap.drawer_poll));
        listViewItems.add(new ItemObject("Create Poll", R.mipmap.drawer_addsociety));
        listViewItems.add(new ItemObject("Archived Polls", R.mipmap.drawer_archive));
        listViewItems.add(new ItemObject("Members", R.mipmap.drawer_members));
        listViewItems.add(new ItemObject("Offers", R.mipmap.drawer_offer));
        listViewItems.add(new ItemObject("Complaint", R.mipmap.drawer_complaint));
        listViewItems.add(new ItemObject("Notifications", R.mipmap.drawer_notification));
        listViewItems.add(new ItemObject("Logout", R.mipmap.drawer_logout));

        return listViewItems;
    }
}
