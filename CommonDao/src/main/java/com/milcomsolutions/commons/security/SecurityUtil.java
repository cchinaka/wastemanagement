package com.milcomsolutions.commons.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;


public class SecurityUtil {

    private static final String MD5_DIGEST = "MD5";

    private static final String DEFAULT_PASSWORD_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12234567890$£@";

    private static final String SHA_512 = "SHA-512";


    public static String generateHashedStringSHA512(String toBeHashed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(SecurityUtil.SHA_512);
        messageDigest.update(toBeHashed.getBytes());
        byte[] echoData = messageDigest.digest();
        String out = StringUtils.EMPTY;
        StringBuilder sb = new StringBuilder();
        for (byte element : echoData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        out = sb.toString();
        return out;
    }


    /**
     * Validates a supplied text password against an encrypted password.
     *
     * @param encPass
     *            The encrypted password
     * @param rawPass
     *            The raw password sent as a text
     * @return true if password is valid, false otherwise
     */
    public static boolean isPasswordValid(String encPass, String rawPass) {
        return SecurityUtil.encodePassword(rawPass).equals(encPass);
    }


    public static String randomPassword(Integer length, String passarray) {
        if (length == null || length == 0) {
            length = 4;
        } // Default Length of Password 4
        if (StringUtils.isBlank(passarray)) {
            passarray = SecurityUtil.DEFAULT_PASSWORD_ARRAY;
        }
        String password = SecurityUtil.generateRandomCaharacters(length, passarray);
        return password;
    }


    public static String generateRandomCaharacters(Integer length) {
        return SecurityUtil.generateRandomCaharacters(length, SecurityUtil.DEFAULT_PASSWORD_ARRAY);
    }


    public static String generateRandomCaharacters(Integer length, String passarray) {
        return RandomStringUtils.random(length, false, true);
    }


    /**
     * Encrypts a text using md5.
     *
     * @param rawPass
     *            The raw password text to encrypt
     *
     * @return the encoded password
     */
    public static String encodePassword(String rawPass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(SecurityUtil.MD5_DIGEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] passwordBytes = rawPass.getBytes();
        byte[] hash = md.digest(passwordBytes);
        return new String(Base64.encodeBase64(hash));
    }


    public static String generateApiKey() {
        String token = SecurityUtil.generateRandomCaharacters(20, SecurityUtil.DEFAULT_PASSWORD_ARRAY).toUpperCase();
        System.out.println(token);
        String bankCode = "GTBBRAND01";
        bankCode = "UPL";
        token = String.format("%s|%s", token, bankCode);
        token = new String(Base64.encodeBase64(token.getBytes()));
        return token;
    }


    public static String generateToken() {
        String token = SecurityUtil.generateRandomCaharacters(10, SecurityUtil.DEFAULT_PASSWORD_ARRAY).toUpperCase();
        token = "GTBR315HO12$123.4";
        // token = "JNFEBYTRDQ";
        EncryptionHelper.encrypt(token);
        long expires = 262800000000l;
        expires = System.currentTimeMillis() + expires;
        String bankCode = "058";
        token = String.format("%s|%s|%s", token, bankCode, expires);
        token = EncryptionHelper.encrypt(token);
        token = new String(Base64.encodeBase64(token.getBytes()));
        return token;
    }


    public static void maind(String[] args) {
        int m = 100;
        int n = 300;
        while (++m < n--) {
            ;
        }
        System.out.println(m);
        Random random = new Random();
        System.out.println("encrypted password[3124]: " + random.nextInt(1000));
        System.out.println("encrypted password[1423]: " + SecurityUtil.encodePassword("5111423511"));
        // 9005522
        // 1177057043846
        // System.out.println("card - " + encodePassword(card));
        // 9593A and 9560I password: REMITA
        // (REMITA) for users ADAT and BABA.
        // ASOMBA
        // P3346
        // P2215
        // P2316
        // P1307
        String usernames = "TESTINITIATOR,TESTFINALAPPROVER";
        String password = "REMITA";
        new StringBuffer("");
        for (String name : usernames.split(",")) {
            // System.out.println(uname + ":" + password + " - " + encodePassword(uname + password));
            System.out.println(String.format("update remita_card set validatepin='%s' where id='%s';", SecurityUtil.encodePassword(name + password), name));
        }
        System.out.println("LANDS5U7RCYKPPO - " + SecurityUtil.encodePassword("PEFS3CRET.1.212"));
        /*
         * for (int a = 1; a <= 3; a++) { for (int b = 1; b <= 3; b++) { userName = String.format("USER%s", a); userpass
         * = String.format("USER%sREMITA1", a); companyId = String.format("MDA%s", b); info =
         * "update remitauser set password='%s', status=1, tokendeviceenabled=0, " +
         * "ROLE_ID='CORPORATE' where username ='%s' and company_id='%s';"; System.out.println(String.format(info,
         * encodePassword(userpass), userName, companyId)); } } for (int i = 200; i <= 200; i++) { // number =
         * String.format("%03d", i); userpass = String.format("LINUXREMITA"); userName = "LINUX"; // info =
         * "update remitauser set password='%s',tokendeviceenabled=0 where username='%s';"; info =
         * "update remitauser set password='%s', status=1 ,tokendeviceenabled=0 where username ='%s' and company_id='011';"
         * ; System.out.println(String.format(info, encodePassword(userpass), userName)); }
         */
        // System.out.println(String.format("Password [%s]", userpass));
        // System.out.println(String.format("Encrypted Password2 [%s]", EncryptionHelper.encrypt(userpass)));
        // System.out.println(String.format("Encrypted Password3 [%s]",
        // EncryptionHelper.decrypt(EncryptionHelper.encrypt(userpass))));
        String time = SecurityUtil.timeStampToCalendar(new java.sql.Timestamp(System.currentTimeMillis()));
        System.out.println("time - " + time);
        for (int i = 0; i < 500; i++) {
            long d = System.currentTimeMillis();
            System.out.println(random.nextInt(1000));
            System.out.println(String.format("%s,05-07-2012 18:00.3,100.00,001004000724016,0010698925,RemitaTest_%s", d + i, d + i));
        }
        System.out.printf("User Token : %s \n", EncryptionHelper.encrypt(args[1]));
        System.out.printf("User Password : %s \n", EncryptionHelper.encrypt(args[2]));
        System.out.printf("Password Hash : %s \n", SecurityUtil.encodePassword(args[0] + args[1]));
    }


    public static String MD5Encode(String rawPass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(SecurityUtil.MD5_DIGEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] passwordBytes = rawPass.getBytes();
        byte[] hash = md.digest(passwordBytes);
        StringBuffer sb = new StringBuffer();
        for (byte element : hash) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    public static String timeStampToCalendar(java.sql.Timestamp ts) {
        java.util.Calendar cally = java.util.Calendar.getInstance();
        cally.setTimeInMillis(ts.getTime());
        SimpleDateFormat sformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sformat.format(cally.getTime());
    }


    public synchronized static Long generateRequestId() {
        return System.currentTimeMillis();
    }


    public static void decodeXML() throws Exception {
        String trace = "3C3F786D6C2076657273696F6E3D2022312E302220656E636F64696E673D225554462D38223F3E3C53696E676C655472616E73666572526571756573743E3C5472616E735265663E383131363134333433333C2F5472616E735265663E3C5472616E73616374696F6E446174653E30322D30372D323031343C2F5472616E73616374696F6E446174653E3C44656269744163636F756E743E373934383334303030303131323C2F44656269744163636F756E743E3C4372656469744163636F756E743E3939393031313135343639373031363C2F4372656469744163636F756E743E3C43757272656E63793E4E474E3C2F43757272656E63793E3C416D6F756E743E363030303C2F416D6F756E743E3C4E6172726174696F6E3E522D31313631343334332F677265656E3C2F4E6172726174696F6E3E3C2F53696E676C655472616E73666572526571756573743E0A383438433033323730423546323242444138434144333031303346443130373531363935323844453930363834324641453742353142384336383530313134313742444243414337463732443237354237444643353233334239343846384441453233413835423234433141433543393335303337304336433036313034304630354343344137333337353331343533373143333133363736373842383733443438334538304533353838454632393137413637303846434641393941364639324343324534424632323830434530343836363932373441414344364245433830464138373236343535423436374234304537343935313041463046324633334631333535413132373133443944323832424143314235314542314243394330353545324630314633323335373742373436353244343533374645443033413745304630324430424243333432453438324331444145413739433032413446373733413544453731344444434646343242324437353538433131464230454545343333444337463030333934313636343143344339303035304432453634414235353435334342343830423241433232443245424633444141384137383439343541454144353936304634444537433134423332413037444236324439433645343945413846313038373432363238363532383835313034413838443438303045334244424235303032453538383231363441383635353231433141454339364231313438463533373444363334434633313830343639344230353042343939393646383142363832413441304536343834373332353943444342334631363531453536334545333145374443453232363038374435314138423232443434323833393231444435343043333242313639453131303841333944333832353145323746453032464346363631424530443146363730433443424532303444394446324339463943353639353741443735314337323138343837364332434533373846413230393135354146434135353238394445433337463635343833324544343235433330424444314132464443383633344444334232323545394141433234394345413636353941373145374430373844343242344646383930323631313531333936443034424531453B";
        byte[] yourBytes = new BigInteger(trace, 16).toByteArray();
        System.out.print(new String(yourBytes, "UTF-8"));
    }


    public static String encrypt(String vNumber) {
        // TODO Auto-generated method stub
        return vNumber;
    }
}
