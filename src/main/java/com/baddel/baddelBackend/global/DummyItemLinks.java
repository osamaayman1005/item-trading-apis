package com.baddel.baddelBackend.global;

import java.util.*;

//TODO remove this shit
public class DummyItemLinks {
    public static List<String> links = new ArrayList<>(List.of(
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-1.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-2.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-3.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-4.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-5.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-6.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-7.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-8.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-9.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-10.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-11.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-12.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-13.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-14.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-15.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-16.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-17.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-18.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-19.jpg",
            "https://wallpapers-clan.com/wp-content/uploads/2022/07/funny-cat-20.jpg"


    ));

    public static String getRandomMedia(){
        Random random = new Random();
        int randomIndex = random.nextInt(links.size());
        return links.get(randomIndex);
    }
}
