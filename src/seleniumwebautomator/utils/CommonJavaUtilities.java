/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author amoi
 */
public class CommonJavaUtilities {

    static final String classname = "CommonJavaUtilities";//for logs

    private static final String logseparator = "| -1 | ";//for logs

    private static Logging log = null;

    static {
        log = new Logging();
    }

    /**
     * Removes white spaces from a string
     *
     * @param string
     * @return
     */
    public static String removeStringSpaces(String string) {

        return string.replaceAll("\\s+", "");
    }

    /**
     * Removes new lines from a string
     *
     * @param string
     * @return
     */
    public static String removeStringLines(String string) {
        return string.replace("\n", "").replace("\r", "");
    }

    /**
     * Removes first sub-string from the string then converts to upper case.
     *
     * @param string original string before removing the substring.
     * @param substring substring to be removed
     * @return string in upper case after removal of the sub string
     */
    public static String removeFirstSubString(String substring, String string) {
        String str = string.toUpperCase().replaceFirst(substring.toUpperCase(), "");
        return str;
    }

    /**
     * Removes all sub-string from the string then converts to upper case.
     *
     * @param string original string before removing the substring.
     * @param substring substring to be removed
     * @return string in upper case after removal of the sub string
     */
    public static String removeAllSubString(String substring, String string) {
        String str = string.toUpperCase().replaceAll(substring.toUpperCase(), "");
        return str;
    }

    /**
     * Removes lines and spaces from a string, converts to upper case.
     *
     * @param string String to new line and remove white spaces
     * @return string after removing line and spaces
     */
    public static String removeStringSpacesAndlines(String string) {
        return removeStringSpaces(removeStringSpaces(string)).toUpperCase();
    }

    /**
     * Send an email with a file attachments to the customer
     *
     * @param mail_protocol mail protocol
     * @param mail_host host for the mail
     * @param mail_port port number for the mail server
     * @param mail_auth authentication flag
     * @param mail_account mail account
     * @param mail_username mail senders username for the
     * @param mail_password mail senders password
     * @param mail_receivers mail receivers
     * @param mail_subject mail subject
     * @param mail_body mail body
     * @param attachmentFilePath file attachment to be sent
     */
    public static void sendEmail(String mail_protocol, String mail_host,
            String mail_port, String mail_auth, String mail_account,
            String mail_username, String mail_password, String mail_receivers,
            String mail_subject, String mail_body, Path attachmentFilePath) {

        String logpreStr = classname + "sendEmail()" + logseparator;

        log.info(logpreStr + "about to send email file attachment \n");

        log.info("File:" + attachmentFilePath + ". Receivers:" + mail_receivers + "\n");

        Properties emailProps = new Properties();
        try {
            emailProps.put("mail.transport.protocol", "smtp");
            emailProps.put("mail.smtp.host", mail_host);
            emailProps.put("mail.smtp.socketFactory.port", mail_port);
            emailProps.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            emailProps.put("mail.smtp.auth", mail_auth);

            Session session = Session.getDefaultInstance(emailProps,
                    new javax.mail.Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication
                        getPasswordAuthentication() {

                    return new javax.mail.PasswordAuthentication(
                            mail_username, mail_password);
                }
            });

            session.setDebug(false);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail_account));
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mail_body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentFilePath.toFile());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachmentFilePath.getFileName().toString());
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail_receivers));
            message.setSubject(mail_subject);

            Transport.send(message);

            log.info(logpreStr + "Email sent successfully... \n");

        } catch (AddressException ae) {

            log.error(logpreStr + "An exception has occured, please confirm that"
                    + " mail address are valid, details:-" + ae + "\n");

        } catch (MessagingException me) {

            log.error(logpreStr + "A messaging exception has occured, details:-"
                    + me + "\n");

        } catch (Exception | Error e) {

            log.error(logpreStr + "An exception has occured, details:- " + e + "\n");

        }
    }

    /**
     * Sleeps the system for specified seconds.
     *
     * @param seconds the sleep time in seconds
     */
    public static void setSleepTime(int seconds) {

        String logPre = classname + "setSleepTime()" + logseparator;

        try {

            Thread.sleep(seconds * 1000);

        } catch (Exception | Error ex) {

            log.info(logPre + "Exception on sleep. Details:" + ex);
        }
    }

    /**
     * Converts scientifically annoted number into a string its decimal
     * equivalent. For example 2.0E6 is converted to 2000000.
     *
     * @param decimalvalue the decimal value between 1 and 10 e.g 1.23344.
     * @param powerof10 the power of 10 e.g 6.
     * @return
     */
    public static String processScientificNumbertoString(String decimalvalue,
            String powerof10) {
        String processed = decimalvalue;
        //count of digits after a decimal point. 
        int decimalenghth = processed.length() - 1;
        //Power of tens for the decimal value
        int power = Integer.parseInt(powerof10);

        int difference = power - decimalenghth;
        /**
         * Append x zeros to the processed where x is the difference.
         * 2.5472826704E11 will return 254728267040
         */
        if (power > decimalenghth) {
            for (int i = 0; i < difference; i++) {
                processed = processed.concat("0");
            }
        } /**
         * Append x zeros to the processed where x is the difference.
         * 2.54728267041E10 will return 25472826704.1
         */
        else if (power < decimalenghth) {
            /**
             * power-decimal length produces -ve value since power len is less
             * decimal len.
             */
            //Get figure before decimal point.
            String prestr = processed.substring(0, power + 1);
            String poststr = processed.replaceFirst(prestr, "");
            processed = prestr + "." + poststr;
        }
        return processed;
    }

    /**
     * Checks whether element equals all list elements.
     *
     * @param expected comparator.
     * @param list items list.
     * @return
     */
    public static boolean assert_AllItemsEquals(Object expected,
            ArrayList<?> list) {

        String logPreStr = classname + "assert_AllItemsEquals()"
                + logseparator;

        String type = list.get(0).getClass().getSimpleName();

        log.info(logPreStr + "expected:-" + expected
                + ".item list size:-" + list.size());

        boolean matchstatus = false;

        if (expected.getClass().getSimpleName().equalsIgnoreCase(type)) {

            if (type.equalsIgnoreCase("Double")) {

                for (Object result : list) {

                    if ((Objects.equals((Double) result, (Double) expected))) {

                        matchstatus = true;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("Integer")) {

                for (Object result : list) {

                    if ((Objects.equals((Integer) result, (Integer) expected))) {

                        matchstatus = true;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("String")) {

                for (Object result : list) {

                    if ((((String) result).compareToIgnoreCase((String) expected) == 0)) {

                        matchstatus = true;

                        break;
                    }
                }
            } else {

                matchstatus = false;

                log.info(logPreStr + "list & minimum value of different data type");
            }
        }

        return matchstatus;
    }

    /**
     * Checks whether 2 array lists are equals.
     *
     * @param dataSet
     * @param comparatorDataSet
     * @return
     */
    public static boolean assert_DataSetEquals(ArrayList<?> dataSet,
            ArrayList<?> comparatorDataSet) {

        String logPreStr = classname + "assert_DataSetEquals()"
                + logseparator;

        ArrayList<?> data_set = dataSet;
        data_set.sort(null);

        ArrayList<?> comparator_DataSet = comparatorDataSet;
        comparator_DataSet.sort(null);

        return data_set.equals(comparator_DataSet);
    }

    /**
     * Checks whether Super-Text contains all the specified sub-strings.
     *
     * @param super_text
     * @param sub_texts
     * @return
     */
    public static boolean assert_TextContainsAllOf(
            String super_text, String... sub_texts) {

        String Super_Text = removeStringSpacesAndlines(super_text);

        for (String text : sub_texts) {

            String sub_text = removeStringSpacesAndlines(text);

            if (!Super_Text.contains(sub_text)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether Super-Text does not contain any of sub-strings.
     *
     * @param super_text
     * @param sub_texts
     * @return false if super-text contains at least one of the strings.
     */
    public static boolean assert_TextContainsNoneOf(
            String super_text, String... sub_texts) {

        String Super_Text = removeStringSpacesAndlines(super_text);

        for (String text : sub_texts) {

            String sub_text = removeStringSpacesAndlines(text);

            if (Super_Text.contains(sub_text)) {
                return false;
            }
        }

        return true;
    }

    /**
     * checks whether element is between minimum and maximum, inclusive.
     *
     * @param minimum
     * @param maximum
     * @param list
     * @return true if all results elements within the range
     */
    public static boolean assert_AllItemsBetween(Object minimum,
            Object maximum, ArrayList<?> list) {

        list.sort(null);

        String logPreStr = classname + "assert_AllItemsBetween()"
                + logseparator;

        log.info(logPreStr + "minimum:-" + minimum
                + ",maximum:-" + maximum + ". list items:-" + list);

        boolean matchstatus = false;

        if ((list.get(0).equals(minimum) || assert_AllItemsMoreThan(minimum, list))
                && (list.get(list.size() - 1).equals(maximum)
                || assert_AllItemsLessThan(maximum, list))) {
            matchstatus = true;

        }

        return matchstatus;
    }

    /**
     * Asserts that all list items are less than the maximum value.
     *
     * @param maximum maximum value expected for any list element.
     * @param list list of items to check.
     * @return false if there exist element(s) more than maximum.
     */
    public static boolean assert_AllItemsLessThan(
            Object maximum, ArrayList<?> list) {

        String logPreStr = classname + "assert_AllItemsLessThan()" + logseparator;

        String type = list.get(0).getClass().getSimpleName();

        log.info(logPreStr + "maximum:-" + maximum
                + ".item list size:-" + list.size());

        boolean matchstatus = true;

        if (maximum.getClass().getSimpleName().equalsIgnoreCase(type)) {

            if (type.equalsIgnoreCase("Double")) {

                for (Object result : list) {

                    if (((Double) result >= (Double) maximum)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("Integer")) {

                for (Object result : list) {

                    if (((Integer) result >= (Integer) maximum)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("String")) {

                for (Object result : list) {

                    if ((((String) result).compareToIgnoreCase((String) maximum) > 0)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else {

                matchstatus = false;

                log.info(logPreStr + "list & maximum value of different data type");
            }
        }

        return matchstatus;
    }

    /**
     * Asserts that all list items are more than the minimum value.
     *
     * @param minimum minimum value expected for any list element.
     * @param list list of items to check.
     * @return false if there exist element(s) less than minimum.
     */
    public static boolean assert_AllItemsMoreThan(
            Object minimum, ArrayList<?> list) {

        String logPreStr = classname + "assertThatAllItemsMoreThan()"
                + logseparator;

        String type = list.get(0).getClass().getSimpleName();

        log.info(logPreStr + "minimum:-" + minimum
                + ".item list size:-" + list.size());

        boolean matchstatus = true;

        if (minimum.getClass().getSimpleName().equalsIgnoreCase(type)) {

            if (type.equalsIgnoreCase("Double")) {

                for (Object result : list) {

                    if (((Double) result <= (Double) minimum)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("Integer")) {

                for (Object result : list) {

                    if (((Integer) result <= (Integer) minimum)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else if (type.equalsIgnoreCase("String")) {

                for (Object result : list) {

                    if ((((String) result).compareToIgnoreCase((String) minimum) < 0)) {

                        matchstatus = false;

                        break;
                    }
                }
            } else {

                matchstatus = false;

                log.info(logPreStr + "list and minimum value of different data type");
            }
        }

        return matchstatus;
    }

    /**
     * Splits a string based on the regular expression, converts to upper case.
     *
     * @param string original string
     * @param splitter regular expression for splitting the string
     * @return string array in upper case after split.
     */
    public static String[] splitString(String string, String splitter) {

        String[] stringarray = string.toUpperCase().split(splitter.toUpperCase());

        log.info(classname + "splitString()" + logseparator + ", Old"
                + " string:" + string + ", is split into " + stringarray.length
                + ". Resulting string array after split:- " + splitter);

        return stringarray;

    }

    /**
     * Filters a numeric substring from a string e.g 2500 from "KES 2,500 Paid".
     *
     * @param originalstring string to be formatted
     * @param prenumericstr string before the numeric figures in the display
     * @param postnumericstr string after the numeric figure in the display
     * @return numeric from non-numeric string.
     */
    public static String convertScientificToDecimal(String originalstring,
            String prenumericstr, String postnumericstr) {

        String logPre = classname + "convertScientificToDecimal()" + logseparator;

        String formatted = originalstring;
        formatted = removeStringSpacesAndlines(formatted);
        formatted = formatted.replaceAll(",", "");

        String prestring = removeStringSpacesAndlines(prenumericstr);
        prestring = prestring.replaceAll(",", "");

        String poststring = removeStringSpacesAndlines(postnumericstr);
        poststring = poststring.replaceAll(",", "");

        formatted = formatted.replaceAll(prestring, "");
        formatted = formatted.replaceAll(poststring, "");

        log.info(logPre + "Scientific:" + originalstring + ", decimal " + formatted);

        return formatted;
    }

    /**
     * converts date into string with specified format.
     *
     * @param date Date to format.
     * @param format date format
     * @return date in the string format.
     */
    public static String convertDateToString(Date date, String format) {

        String logPre = classname + "convertDateToString()" + logseparator;

        String datetime = null;

        try {

            return getDateFormatter(format).format(date);

        } catch (Exception e) {

            log.error(logPre + "Error converting date:" + date.toString()
                    + "with format:" + format + "details:-" + e);
        }
        return datetime;
    }

    /**
     * defines the date format
     *
     * @param format
     * @return
     */
    public static SimpleDateFormat getDateFormatter(String format) {

        return new SimpleDateFormat(format);
    }

    /**
     * converts date in to specified format.
     *
     * @param date date to convert
     * @param format format to convert to
     * @return
     */
    public static Date formatDate(Date date, String format) {

        String preLog = classname + "getDateInFormat()" + logseparator;

        try {
            String dateString = getDateFormatter(format).format(date);

            return convertStringtoDate(dateString, format);
        } catch (Exception | Error e) {

            log.error(preLog + "exception, details:" + e);

            return null;
        }

    }

    /**
     * Converts string to its date equivalent.
     *
     * @param dateString date in string
     * @param format format of date to apply on converting
     * @return
     */
    public static Date convertStringtoDate(String dateString, String format) {

        String preLog = classname + "convertStringtoDate()" + logseparator;

        Date date = null;

        try {

            date = getDateFormatter(format).parse(dateString);

        } catch (Exception | Error e) {

            log.error(preLog + "exception, details:" + e);
        }

        return date;
    }

    /**
     * counts the number of month between start date and end date
     *
     * @param start
     * @param end
     * @return
     */
    public static int getMonthsCountBetweenDates(Date start, Date end) {

        Calendar startCalendar = new GregorianCalendar();

        startCalendar.setTime(start);

        Calendar endCalendar = new GregorianCalendar();

        endCalendar.setTime(end);

        int diffYear = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);

        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        return diffMonth;

    }

    /**
     * counts the number of month between start date and end date
     *
     * @param start start date
     * @param end end date
     * @return
     */
    public static int getMonthsCountBetweenDates(String start, String end) {

        Calendar startCalendar = new GregorianCalendar();

        startCalendar.setTime(convertStringtoDate(start, "dd/MM/yyyy"));

        Calendar endCalendar = new GregorianCalendar();

        endCalendar.setTime(convertStringtoDate(end, "dd/MM/yyyy"));

        int diffYear = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);

        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        return diffMonth;

    }
}
