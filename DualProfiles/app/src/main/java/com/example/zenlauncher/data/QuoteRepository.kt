package com.example.zenlauncher.data

import com.example.zenlauncher.model.Quote
import java.util.Calendar
import kotlin.random.Random

object QuoteRepository {

    private val quotes = listOf(
        Quote("The only way to do great work is to love what you do.", "", ""),
        Quote("In the middle of difficulty lies opportunity.", "", ""),
        Quote("What you do today can improve all your tomorrows.", "", ""),
        Quote("The mind is everything. What you think you become.", "", ""),
        Quote("It does not matter how slowly you go as long as you do not stop.", "", ""),
        Quote("Believe you can and you are halfway there.", "", ""),
        Quote("The future belongs to those who believe in the beauty of their dreams.", "", ""),
        Quote("You are never too old to set another goal or to dream a new dream.", "", ""),
        Quote("Happiness is not something ready made. It comes from your own actions.", "", ""),
        Quote("The best time to plant a tree was 20 years ago. The second best time is now.", "", ""),
        Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "", ""),
        Quote("The only impossible journey is the one you never begin.", "", ""),
        Quote("Your limitation — it is only your imagination.", "", ""),
        Quote("Push yourself, because no one else is going to do it for you.", "", ""),
        Quote("Great things never come from comfort zones.", "", ""),
        Quote("Dream it. Wish it. Do it.", "", ""),
        Quote("Success does not just find you. You have to go out and get it.", "", ""),
        Quote("The harder you work for something, the greater you will feel when you achieve it.", "", ""),
        Quote("Do not stop when you are tired. Stop when you are done.", "", ""),
        Quote("Wake up with determination. Go to bed with satisfaction.", "", ""),
        Quote("Little things make big days.", "", ""),
        Quote("It is going to be hard, but hard does not mean impossible.", "", ""),
        Quote("Do not wait for opportunity. Create it.", "", ""),
        Quote("Sometimes we are tested not to show our weaknesses, but to discover our strengths.", "", ""),
        Quote("The key to success is to focus on goals, not obstacles.", "", ""),
        Quote("Dream bigger. Do bigger.", "", ""),
        Quote("Do not be afraid to give up the good to go for the great.", "", ""),
        Quote("A journey of a thousand miles begins with a single step.", "", ""),
        Quote("The secret of getting ahead is getting started.", "", ""),
        Quote("It always seems impossible until it is done.", "", ""),
        Quote("Quality is not an act, it is a habit.", "", ""),
        Quote("You miss 100 percent of the shots you do not take.", "", ""),
        Quote("Whether you think you can or you think you can not, you are right.", "", ""),
        Quote("The best revenge is massive success.", "", ""),
        Quote("I have not failed. I have just found 10,000 ways that will not work.", "", ""),
        Quote("The only person you are destined to become is the person you decide to be.", "", ""),
        Quote("Everything you have ever wanted is on the other side of fear.", "", ""),
        Quote("What lies behind us and what lies before us are tiny matters compared to what lies within us.", "", ""),
        Quote("Life is 10 percent what happens to us and 90 percent how we react to it.", "", ""),
        Quote("The best way to predict the future is to create it.", "", ""),
        Quote("You are braver than you believe, stronger than you seem, and smarter than you think.", "", ""),
        Quote("The only limit to our realization of tomorrow is our doubts of today.", "", ""),
        Quote("Act as if what you do makes a difference. It does.", "", ""),
        Quote("What you get by achieving your goals is not as important as what you become by achieving your goals.", "", ""),
        Quote("Hardships often prepare ordinary people for an extraordinary destiny.", "", ""),
        Quote("Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle.", "", ""),
        Quote("The way to get started is to quit talking and begin doing.", "", ""),
        Quote("If you set your goals ridiculously high and it is a failure, you will fail above everyone else success.", "", ""),
        Quote("You do not have to be great to start, but you have to start to be great.", "", ""),
        Quote("The difference between ordinary and extraordinary is that little extra.", "", ""),
        Quote("If you want to achieve greatness stop asking for permission.", "", ""),
        Quote("Do what you can, with what you have, where you are.", "", ""),
        Quote("We may encounter many defeats but we must not be defeated.", "", ""),
        Quote("Security is mostly a superstition. Life is either a daring adventure or nothing.", "", ""),
        Quote("Keep your face always toward the sunshine, and shadows will fall behind you.", "", ""),
        Quote("It is during our darkest moments that we must focus to see the light.", "", ""),
        Quote("Whoever is happy will make others happy too.", "", ""),
        Quote("You will face many defeats in life, but never let yourself be defeated.", "", ""),
        Quote("The purpose of our lives is to be happy.", "", ""),
        Quote("Life is what happens when you are busy making other plans.", "", ""),
        Quote("Get busy living or get busy dying.", "", ""),
        Quote("You only live once, but if you do it right, once is enough.", "", ""),
        Quote("Many of life failures are people who did not realize how close they were to success when they gave up.", "", ""),
        Quote("The way I see it, if you want the rainbow, you gotta put up with the rain.", "", ""),
        Quote("The mind is its own place, and in itself can make a heaven of hell, a hell of heaven.", "", ""),
        Quote("In the depth of winter, I finally learned that within me there lay an invincible summer.", "", ""),
        Quote("Life is really simple, but we insist on making it complicated.", "", ""),
        Quote("May you live all the days of your life.", "", ""),
        Quote("The true secret of happiness lies in taking a genuine interest in all the details of daily life.", "", ""),
        Quote("Very little is needed to make a happy life; it is all within yourself.", "", ""),
        Quote("Happiness depends upon ourselves.", "", ""),
        Quote("Action is the foundational key to all success.", "", "")
    )

    private var dayOfYear = -1
    private var todayQuote: Quote? = null

    fun getDailyQuote(): Quote {
        val today = Calendar.getInstance()
        val doy = today.get(Calendar.DAY_OF_YEAR)
        if (doy != dayOfYear || todayQuote == null) {
            dayOfYear = doy
            todayQuote = quotes[doy % quotes.size]
        }
        return todayQuote!!
    }

    fun getQuoteCount(): Int = quotes.size
}
