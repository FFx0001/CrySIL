package obj;

public class CK_ATTRIBUTE_TYPE {
	
	public final static long CKF_ARRAY_ATTRIBUTE    =0x40000000; //just needed here :)
	public static final long CKA_CLASS              =0x00000000;
	public static final long CKA_TOKEN              =0x00000001;
	public static final long CKA_PRIVATE            =0x00000002;
	public static final long CKA_LABEL              =0x00000003;
	public static final long CKA_APPLICATION        =0x00000010;
	public static final long CKA_VALUE              =0x00000011;
	public static final long CKA_OBJECT_ID          =0x00000012;
	public static final long CKA_CERTIFICATE_TYPE   =0x00000080;
	public static final long CKA_ISSUER             =0x00000081;
	public static final long CKA_SERIAL_NUMBER      =0x00000082;
	public static final long CKA_AC_ISSUER          =0x00000083;
	public static final long CKA_OWNER              =0x00000084;
	public static final long CKA_ATTR_TYPES         =0x00000085;
	public static final long CKA_TRUSTED            =0x00000086;
	public static final long CKA_CERTIFICATE_CATEGORY        =0x00000087;
	public static final long CKA_JAVA_MIDP_SECURITY_DOMAIN   =0x00000088;
	public static final long CKA_URL                         =0x00000089;
	public static final long CKA_HASH_OF_SUBJECT_PUBLIC_KEY  =0x0000008A;
	public static final long CKA_HASH_OF_ISSUER_PUBLIC_KEY   =0x0000008B;
	public static final long CKA_CHECK_VALUE                 =0x00000090;
	public static final long CKA_KEY_TYPE           =0x00000100;
	public static final long CKA_SUBJECT            =0x00000101;
	public static final long CKA_ID                 =0x00000102;
	public static final long CKA_SENSITIVE          =0x00000103;
	public static final long CKA_ENCRYPT            =0x00000104;
	public static final long CKA_DECRYPT            =0x00000105;
	public static final long CKA_WRAP               =0x00000106;
	public static final long CKA_UNWRAP             =0x00000107;
	public static final long CKA_SIGN               =0x00000108;
	public static final long CKA_SIGN_RECOVER       =0x00000109;
	public static final long CKA_VERIFY             =0x0000010A;
	public static final long CKA_VERIFY_RECOVER     =0x0000010B;
	public static final long CKA_DERIVE             =0x0000010C;
	public static final long CKA_START_DATE         =0x00000110;
	public static final long CKA_END_DATE           =0x00000111;
	public static final long CKA_MODULUS            =0x00000120;
	public static final long CKA_MODULUS_BITS       =0x00000121;
	public static final long CKA_PUBLIC_EXPONENT    =0x00000122;
	public static final long CKA_PRIVATE_EXPONENT   =0x00000123;
	public static final long CKA_PRIME_1            =0x00000124;
	public static final long CKA_PRIME_2            =0x00000125;
	public static final long CKA_EXPONENT_1         =0x00000126;
	public static final long CKA_EXPONENT_2         =0x00000127;
	public static final long CKA_COEFFICIENT        =0x00000128;
	public static final long CKA_PRIME              =0x00000130;
	public static final long CKA_SUBPRIME           =0x00000131;
	public static final long CKA_BASE               =0x00000132;
	public static final long CKA_PRIME_BITS         =0x00000133;
	public static final long CKA_SUBPRIME_BITS      =0x00000134;
	public static final long CKA_SUB_PRIME_BITS     =CKA_SUBPRIME_BITS;
	public static final long CKA_VALUE_BITS         =0x00000160;
	public static final long CKA_VALUE_LEN          =0x00000161;
	public static final long CKA_EXTRACTABLE        =0x00000162;
	public static final long CKA_LOCAL              =0x00000163;
	public static final long CKA_NEVER_EXTRACTABLE  =0x00000164;
	public static final long CKA_ALWAYS_SENSITIVE   =0x00000165;
	public static final long CKA_KEY_GEN_MECHANISM  =0x00000166;
	public static final long CKA_MODIFIABLE         =0x00000170;
	public static final long CKA_ECDSA_PARAMS       =0x00000180;
	public static final long CKA_EC_PARAMS          =0x00000180;
	public static final long CKA_EC_POINT           =0x00000181;
	public static final long CKA_SECONDARY_AUTH     =0x00000200;
	public static final long CKA_AUTH_PIN_FLAGS     =0x00000201;
	public static final long CKA_ALWAYS_AUTHENTICATE  =0x00000202;
	public static final long CKA_WRAP_WITH_TRUSTED    =0x00000210;
	public static final long CKA_WRAP_TEMPLATE       =(CKF_ARRAY_ATTRIBUTE|0x00000211);
	public static final long CKA_UNWRAP_TEMPLATE     = (CKF_ARRAY_ATTRIBUTE|0x00000212);
	public static final long CKA_OTP_FORMAT                =0x00000220;
	public static final long CKA_OTP_LENGTH                =0x00000221;
	public static final long CKA_OTP_TIME_INTERVAL         =0x00000222;
	public static final long CKA_OTP_USER_FRIENDLY_MODE    =0x00000223;
	public static final long CKA_OTP_CHALLENGE_REQUIREMENT =0x00000224;
	public static final long CKA_OTP_TIME_REQUIREMENT      =0x00000225;
	public static final long CKA_OTP_COUNTER_REQUIREMENT   =0x00000226;
	public static final long CKA_OTP_PIN_REQUIREMENT       =0x00000227;
	public static final long CKA_OTP_COUNTER               =0x0000022E;
	public static final long CKA_OTP_TIME                  =0x0000022F;
	public static final long CKA_OTP_USER_IDENTIFIER       =0x0000022A;
	public static final long CKA_OTP_SERVICE_IDENTIFIER    =0x0000022B;
	public static final long CKA_OTP_SERVICE_LOGO          =0x0000022C;
	public static final long CKA_OTP_SERVICE_LOGO_TYPE     =0x0000022D;
	public static final long CKA_HW_FEATURE_TYPE    =0x00000300;
	public static final long CKA_RESET_ON_INIT      =0x00000301;
	public static final long CKA_HAS_RESET          =0x00000302;
	public static final long CKA_PIXEL_X                     =0x00000400;
	public static final long CKA_PIXEL_Y                     =0x00000401;
	public static final long CKA_RESOLUTION                  =0x00000402;
	public static final long CKA_CHAR_ROWS                   =0x00000403;
	public static final long CKA_CHAR_COLUMNS                =0x00000404;
	public static final long CKA_COLOR                       =0x00000405;
	public static final long CKA_BITS_PER_PIXEL              =0x00000406;
	public static final long CKA_CHAR_SETS                   =0x00000480;
	public static final long CKA_ENCODING_METHODS            =0x00000481;
	public static final long CKA_MIME_TYPES                  =0x00000482;
	public static final long CKA_MECHANISM_TYPE              =0x00000500;
	public static final long CKA_REQUIRED_CMS_ATTRIBUTES     =0x00000501;
	public static final long CKA_DEFAULT_CMS_ATTRIBUTES      =0x00000502;
	public static final long CKA_SUPPORTED_CMS_ATTRIBUTES    =0x00000503;
	public static final long CKA_ALLOWED_MECHANISMS         = (CKF_ARRAY_ATTRIBUTE|0x00000600);
	public static final long CKA_VENDOR_DEFINED     =0x80000000;


}