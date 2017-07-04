package com.example.android.miwok;


public class Word {
    private static final int IMAGE_ID_NOT_PASSED = -1;
    /**
     * for miwok Translation
     */
    private String miwokTrans;
    /**
     * for default words
     */
    private String defaultTrans;
    /**
     * for images for the corresponding translation
     */
    private int mImgResourceId = IMAGE_ID_NOT_PASSED;
    private int mAudioResourceId;

    public Word(String passedMT, String passedDT, int passedIRI, int passedARI) {
        miwokTrans = passedMT;
        defaultTrans = passedDT;
        mImgResourceId = passedIRI;
        mAudioResourceId = passedARI;
    }

    public Word(String passedMT, String passedDT, int passedARI) {
        miwokTrans = passedMT;
        defaultTrans = passedDT;
        mAudioResourceId = passedARI;
    }

    @Override
    public String toString() {
        return "Word{" +
                "miwokTrans='" + miwokTrans + '\'' +
                ", defaultTrans='" + defaultTrans + '\'' +
                ", mImgResourceId=" + mImgResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }

    public String getMiwokTrans() {
        return miwokTrans;
    }

    public String getDefaultTrans() {
        return defaultTrans;
    }

    public int getImageResourceId() {
        return mImgResourceId;
    }

    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    public boolean hasImage() {
        return mImgResourceId != IMAGE_ID_NOT_PASSED;
    }
}
