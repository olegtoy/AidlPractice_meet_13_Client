// IDataInterface.aidl
package com.practice.olegtojgildin.aidlpractice_meet_13;

// Declare any non-default types here with import statements

interface IDataInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     String getDataText();

     void saveDataText(String text);
}
