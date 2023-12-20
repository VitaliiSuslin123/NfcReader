package com.example.nfccardreader.iso7816emv;


import com.example.nfccardreader.enums.TagTypeEnum;
import com.example.nfccardreader.enums.TagValueTypeEnum;

public interface ITag {

    enum Class {
        UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE
    }

    boolean isConstructed();

    byte[] getTagBytes();

    String getName();

    String getDescription();

    TagTypeEnum getType();

    TagValueTypeEnum getTagValueType();

    Class getTagClass();

    int getNumTagBytes();

}
