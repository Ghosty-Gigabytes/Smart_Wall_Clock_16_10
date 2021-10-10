# Smart_Wall_Clock_16_10
Smart Wall Clock 16:10 
This project was intended to be a replacement for traditional wall clocks. The implementation of this project is easy, load up a copy of Androidx86 on a old laptop, compile the project and install it on the laptop and play around with the Hardware to elegantly mount it on a wall. 

In the beginning the app included a Weather widget, an analog clock, a digital HH:MM clock and a date widget but later on various functionality had been added to the project, such as 

-Hourly Ding-Dong notification, 

-New Year count-down, 

-Animated Background, 

-Ability to change the location of the Weather Widget, 

-Network access and Power State icons, 

-Ability to keep the device awake while its running, 

-Ability to start the app when the device boots up (doesn't work on some devices), 

-Enhanced Fullscreen mode

-Automatic Brightness adjustment according to the time (Timing and brightness level hardcoded)

-Second's loop for Analog Clock

The weather widget was the toughest to implement as I had very little knowledge of Java in the beginning and managing Weather API data wasn't easy either, so I used a WebView to load MSN Weather Website for showing weather, earlier the URL was hardcoded but now it can be changed by the user.

Upcoming features include,

-Proper Weather Widget

-Ability to change the background animation

-Ability to change the Brightness adjustment parameters

-Enhanced Second's Loop

-Responsive Design

-Preventing the animation stopping after Main Activty is reopened

The project isn't a responsive design, which means that only screens which have native aspect ratio of 16:10 can only properly utilize the app. I had unknowingly started with a 16:9 design but later had to convert it to a 16:10 one.

I had started working on the project on June 10th 2021 and after almost 3 months it has reached a state where it can be implemented with ease and can be used flawlessly.

