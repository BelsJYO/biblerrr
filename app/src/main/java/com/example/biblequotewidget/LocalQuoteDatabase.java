package com.example.biblequotewidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A local database of Bible quotes to use when API is unavailable or for faster loading
 */
public class LocalQuoteDatabase {
    
    // Map of themes to lists of quotes
    private static final Map<String, List<BibleQuote>> QUOTES_BY_THEME = new HashMap<>();
    
    // Initialize the database with quotes
    static {
        initializeHopeQuotes();
        initializeLoveQuotes();
        initializeStrengthQuotes();
        initializeWisdomQuotes();
        initializeComfortQuotes();
        initializeMotivationQuotes();
        initializePhilosophicalQuotes();
    }
    
    /**
     * Get a random quote for the specified theme
     * @param theme The theme to get a quote for
     * @return A random BibleQuote for the theme, or from another theme if not found
     */
    public static BibleQuote getRandomQuote(String theme) {
        List<BibleQuote> quotes = QUOTES_BY_THEME.get(theme.toLowerCase());
        
        // If theme not found or empty, use wisdom theme as default
        if (quotes == null || quotes.isEmpty()) {
            quotes = QUOTES_BY_THEME.get("wisdom");
        }
        
        // Get a random quote from the list
        Random random = new Random();
        int index = random.nextInt(quotes.size());
        return quotes.get(index);
    }
    
    /**
     * Get a random quote from any theme
     * @return A random BibleQuote from any theme
     */
    public static BibleQuote getRandomQuote() {
        // Get all themes
        List<String> themes = new ArrayList<>(QUOTES_BY_THEME.keySet());
        
        // Select a random theme
        Random random = new Random();
        String randomTheme = themes.get(random.nextInt(themes.size()));
        
        // Get a random quote from that theme
        return getRandomQuote(randomTheme);
    }
    
    /**
     * Bible Quote data class
     */
    public static class BibleQuote {
        private final String text;
        private final String reference;
        private final String theme;
        
        public BibleQuote(String text, String reference, String theme) {
            this.text = text;
            this.reference = reference;
            this.theme = theme;
        }
        
        public String getText() {
            return text;
        }
        
        public String getReference() {
            return reference;
        }
        
        public String getTheme() {
            return theme;
        }
    }
    
    // Initialize quotes for the "hope" theme
    private static void initializeHopeQuotes() {
        List<BibleQuote> hopeQuotes = new ArrayList<>();
        
        hopeQuotes.add(new BibleQuote(
            "For I know the plans I have for you, declares the LORD, plans for welfare and not for evil, to give you a future and a hope.",
            "Jeremiah 29:11",
            "hope"
        ));
        
        hopeQuotes.add(new BibleQuote(
            "May the God of hope fill you with all joy and peace in believing, so that by the power of the Holy Spirit you may abound in hope.",
            "Romans 15:13",
            "hope"
        ));
        
        hopeQuotes.add(new BibleQuote(
            "But they who wait for the LORD shall renew their strength; they shall mount up with wings like eagles; they shall run and not be weary; they shall walk and not faint.",
            "Isaiah 40:31",
            "hope"
        ));
        
        hopeQuotes.add(new BibleQuote(
            "For whatever was written in former days was written for our instruction, that through endurance and through the encouragement of the Scriptures we might have hope.",
            "Romans 15:4",
            "hope"
        ));
        
        hopeQuotes.add(new BibleQuote(
            "Rejoice in hope, be patient in tribulation, be constant in prayer.",
            "Romans 12:12",
            "hope"
        ));
        
        QUOTES_BY_THEME.put("hope", hopeQuotes);
    }
    
    // Initialize quotes for the "love" theme
    private static void initializeLoveQuotes() {
        List<BibleQuote> loveQuotes = new ArrayList<>();
        
        loveQuotes.add(new BibleQuote(
            "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life.",
            "John 3:16",
            "love"
        ));
        
        loveQuotes.add(new BibleQuote(
            "Love is patient and kind; love does not envy or boast; it is not arrogant or rude. It does not insist on its own way; it is not irritable or resentful; it does not rejoice at wrongdoing, but rejoices with the truth.",
            "1 Corinthians 13:4-6",
            "love"
        ));
        
        loveQuotes.add(new BibleQuote(
            "We love because he first loved us.",
            "1 John 4:19",
            "love"
        ));
        
        loveQuotes.add(new BibleQuote(
            "Greater love has no one than this, that someone lay down his life for his friends.",
            "John 15:13",
            "love"
        ));
        
        loveQuotes.add(new BibleQuote(
            "And above all these put on love, which binds everything together in perfect harmony.",
            "Colossians 3:14",
            "love"
        ));
        
        QUOTES_BY_THEME.put("love", loveQuotes);
    }
    
    // Initialize quotes for the "strength" theme
    private static void initializeStrengthQuotes() {
        List<BibleQuote> strengthQuotes = new ArrayList<>();
        
        strengthQuotes.add(new BibleQuote(
            "I can do all things through him who strengthens me.",
            "Philippians 4:13",
            "strength"
        ));
        
        strengthQuotes.add(new BibleQuote(
            "The LORD is my strength and my shield; in him my heart trusts, and I am helped; my heart exults, and with my song I give thanks to him.",
            "Psalm 28:7",
            "strength"
        ));
        
        strengthQuotes.add(new BibleQuote(
            "Be strong and courageous. Do not fear or be in dread of them, for it is the LORD your God who goes with you. He will not leave you or forsake you.",
            "Deuteronomy 31:6",
            "strength"
        ));
        
        strengthQuotes.add(new BibleQuote(
            "But he said to me, 'My grace is sufficient for you, for my power is made perfect in weakness.' Therefore I will boast all the more gladly of my weaknesses, so that the power of Christ may rest upon me.",
            "2 Corinthians 12:9",
            "strength"
        ));
        
        strengthQuotes.add(new BibleQuote(
            "Fear not, for I am with you; be not dismayed, for I am your God; I will strengthen you, I will help you, I will uphold you with my righteous right hand.",
            "Isaiah 41:10",
            "strength"
        ));
        
        QUOTES_BY_THEME.put("strength", strengthQuotes);
    }
    
    // Initialize quotes for the "wisdom" theme
    private static void initializeWisdomQuotes() {
        List<BibleQuote> wisdomQuotes = new ArrayList<>();
        
        wisdomQuotes.add(new BibleQuote(
            "The fear of the LORD is the beginning of wisdom, and the knowledge of the Holy One is insight.",
            "Proverbs 9:10",
            "wisdom"
        ));
        
        wisdomQuotes.add(new BibleQuote(
            "If any of you lacks wisdom, let him ask God, who gives generously to all without reproach, and it will be given him.",
            "James 1:5",
            "wisdom"
        ));
        
        wisdomQuotes.add(new BibleQuote(
            "Blessed is the one who finds wisdom, and the one who gets understanding, for the gain from her is better than gain from silver and her profit better than gold.",
            "Proverbs 3:13-14",
            "wisdom"
        ));
        
        wisdomQuotes.add(new BibleQuote(
            "For the LORD gives wisdom; from his mouth come knowledge and understanding.",
            "Proverbs 2:6",
            "wisdom"
        ));
        
        wisdomQuotes.add(new BibleQuote(
            "The way of a fool is right in his own eyes, but a wise man listens to advice.",
            "Proverbs 12:15",
            "wisdom"
        ));
        
        QUOTES_BY_THEME.put("wisdom", wisdomQuotes);
    }
    
    // Initialize quotes for the "comfort" theme
    private static void initializeComfortQuotes() {
        List<BibleQuote> comfortQuotes = new ArrayList<>();
        
        comfortQuotes.add(new BibleQuote(
            "Blessed are those who mourn, for they shall be comforted.",
            "Matthew 5:4",
            "comfort"
        ));
        
        comfortQuotes.add(new BibleQuote(
            "Come to me, all who labor and are heavy laden, and I will give you rest.",
            "Matthew 11:28",
            "comfort"
        ));
        
        comfortQuotes.add(new BibleQuote(
            "The LORD is near to the brokenhearted and saves the crushed in spirit.",
            "Psalm 34:18",
            "comfort"
        ));
        
        comfortQuotes.add(new BibleQuote(
            "Blessed be the God and Father of our Lord Jesus Christ, the Father of mercies and God of all comfort, who comforts us in all our affliction.",
            "2 Corinthians 1:3-4",
            "comfort"
        ));
        
        comfortQuotes.add(new BibleQuote(
            "He will wipe away every tear from their eyes, and death shall be no more, neither shall there be mourning, nor crying, nor pain anymore, for the former things have passed away.",
            "Revelation 21:4",
            "comfort"
        ));
        
        QUOTES_BY_THEME.put("comfort", comfortQuotes);
    }
    
    // Initialize quotes for the "motivation" theme
    private static void initializeMotivationQuotes() {
        List<BibleQuote> motivationQuotes = new ArrayList<>();
        
        motivationQuotes.add(new BibleQuote(
            "And let us not grow weary of doing good, for in due season we will reap, if we do not give up.",
            "Galatians 6:9",
            "motivation"
        ));
        
        motivationQuotes.add(new BibleQuote(
            "Therefore, my beloved brothers, be steadfast, immovable, always abounding in the work of the Lord, knowing that in the Lord your labor is not in vain.",
            "1 Corinthians 15:58",
            "motivation"
        ));
        
        motivationQuotes.add(new BibleQuote(
            "But as for you, be strong and do not give up, for your work will be rewarded.",
            "2 Chronicles 15:7",
            "motivation"
        ));
        
        motivationQuotes.add(new BibleQuote(
            "Have I not commanded you? Be strong and courageous. Do not be frightened, and do not be dismayed, for the LORD your God is with you wherever you go.",
            "Joshua 1:9",
            "motivation"
        ));
        
        motivationQuotes.add(new BibleQuote(
            "For I am sure that neither death nor life, nor angels nor rulers, nor things present nor things to come, nor powers, nor height nor depth, nor anything else in all creation, will be able to separate us from the love of God in Christ Jesus our Lord.",
            "Romans 8:38-39",
            "motivation"
        ));
        
        QUOTES_BY_THEME.put("motivation", motivationQuotes);
    }
    
    // Initialize quotes for the "philosophical" theme
    private static void initializePhilosophicalQuotes() {
        List<BibleQuote> philosophicalQuotes = new ArrayList<>();
        
        philosophicalQuotes.add(new BibleQuote(
            "For now we see in a mirror dimly, but then face to face. Now I know in part; then I shall know fully, even as I have been fully known.",
            "1 Corinthians 13:12",
            "philosophical"
        ));
        
        philosophicalQuotes.add(new BibleQuote(
            "What has been is what will be, and what has been done is what will be done, and there is nothing new under the sun.",
            "Ecclesiastes 1:9",
            "philosophical"
        ));
        
        philosophicalQuotes.add(new BibleQuote(
            "For the invisible things of him from the creation of the world are clearly seen, being understood by the things that are made, even his eternal power and Godhead; so that they are without excuse.",
            "Romans 1:20",
            "philosophical"
        ));
        
        philosophicalQuotes.add(new BibleQuote(
            "For we brought nothing into the world, and we cannot take anything out of the world.",
            "1 Timothy 6:7",
            "philosophical"
        ));
        
        philosophicalQuotes.add(new BibleQuote(
            "Vanity of vanities, says the Preacher, vanity of vanities! All is vanity. What does man gain by all the toil at which he toils under the sun?",
            "Ecclesiastes 1:2-3",
            "philosophical"
        ));
        
        QUOTES_BY_THEME.put("philosophical", philosophicalQuotes);
    }
}
