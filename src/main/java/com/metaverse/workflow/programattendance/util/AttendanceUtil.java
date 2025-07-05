package com.metaverse.workflow.programattendance.util;

/**
 * Utility class for attendance-related operations
 */
public class AttendanceUtil {

    /**
     * Converts a Character array to a String
     * 
     * @param attendanceData Character array representing attendance data
     * @return String representation of the attendance data
     */
    public static String characterArrayToString(Character[] attendanceData) {
        if (attendanceData == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder(attendanceData.length);
        for (Character ch : attendanceData) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Converts a String to a Character array
     * 
     * @param attendanceData String representation of attendance data
     * @return Character array representation of the attendance data
     */
    public static Character[] stringToCharacterArray(String attendanceData) {
        if (attendanceData == null || attendanceData.isEmpty()) {
            return new Character[0];
        }
        
        Character[] charArray = new Character[attendanceData.length()];
        for (int i = 0; i < attendanceData.length(); i++) {
            charArray[i] = attendanceData.charAt(i);
        }
        return charArray;
    }
}