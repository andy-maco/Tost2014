package com.maknc.tost2014;

/**
 * Settings class
 */
public class Config {
	
	/* AdMob */
	public static final String AD_UNIT_ID = "a152b95c535dbe7";
	
	/* Temp values for deatail viewpager */
	public static final int NUM_ITEMS = 10;
	
	public static final String PREFS = "com.maknc.tost2014.PREFERENCE_FILE_KEY";
	public static final String PREFS_FAVORITES_KEY = "favorites";
	
	/* Intents tags */
	protected static final String TAG_AUTHOR = "com.andreimak.brightquotes.TAG_AUTHOR";
	protected static final String TAG_AUTHOR_IMAGE = "com.andreimak.brightquotes.TAG_AUTHOR_IMAGE";
	
	public final static String TAG_TOST_TEXT = "com.maknc.tost2014.TAG_TOST_TEXT";
	public final static String TAG_TOST_ID = "com.maknc.tost2014.TAG_TOST_ID";

	/* JSON load settings */
	protected static final String JSON_URL = "bright_quotes.json";
	protected static final String JSON_AUTHORS_ARRAY = "authors";
	protected static final String JSON_AUTHOR = "authorName";
	protected static final String JSON_IMAGE = "image";
	protected static final String JSON_QUOTES = "quotes";
	
	public static final String JSON_TOST_URL = "newtost2014.json";
	public static final String JSON_TOSTS_ARRAY = "tosts";
	public static final String JSON_ID = "id";
	public static final String JSON_TEXT = "text";
	public static final String JSON_TEXT_TAG = "tag";
	

	/* HashMap keys */
	public static final String KEY_AUTHOR_NAME = "author_name";
	public static final String KEY_AUTHOR_IMAGE = "author_image";
	public static final String KEY_QUOTES_QUANTITY = "quotes_quantity";
	public static final String KEY_QUOTE_TEXT = "quote_text";
	
	public static final String KEY_ID = "tost_id";
	public static final String KEY_TEXT = "tost_text";
	public static final String KEY_FAV = "tost_fav";
	public static final String KEY_TEXT_TAG = "tost_tag";
	

}
