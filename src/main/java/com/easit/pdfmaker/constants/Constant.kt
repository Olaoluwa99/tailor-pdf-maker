package com.easit.pdfmaker.constants

import androidx.compose.ui.graphics.Color

object Constant {
    const val USER = "user"

    const val BUILDER_TAG = "*#-BUILDER_TYPE-#*"

    private const val REMOTE_CONFIG = "new_version_1_0_13"
    const val UPDATE_TEXT_MAIN = "${REMOTE_CONFIG}_main"
    const val UPDATE_TEXT_SUB = "${REMOTE_CONFIG}_sub"
    const val SUPPORTERS_LIST = "supporters_list"

    const val FULL_NAME = "Name"
    const val EMAIL = "Email"
    const val PHONE_NO = "Phone"
    const val LOCATION = "Location"
    const val EXPERIENCES = "Experiences"
    const val EDUCATION = "Education"
    const val PROJECTS = "Projects"
    const val SKILLS = "Skills"
    const val CERTIFICATIONS = "Certifications"
    const val HOBBIES = "Hobbies"
    const val OTHERS = "Others"

    const val PADDING = 16
    const val HISTORY_RADIUS = 12
    const val DEFAULT_BUTTON_PADDING = 12

    //Detail/Profile Screen
    const val DETAIL_INNER_RADIUS = 8

    //SignUp/In
    const val VERTICAL_SPACING = 6
    const val HEADER_SIZE = 17

    const val EMAIL_TITLE = "E-mail"
    const val FULL_NAME_TITLE = "Full name"
    const val PASSWORD = "Password"
    const val CONFIRM_PASSWORD = "Confirm password"

    const val TAILORED = 0
    const val SINGLE = 1

    const val INACTIVE = 0
    const val LOADING = 1
    const val SUCCESS = 2
    const val FAILURE = 3
    const val PENDING = 10
    const val INVALID_RESUME_STYLE = 9999
    const val INITIALIZING = 4
    const val FAILED_HISTORY = 5

    val RED = Color(red = 219, green = 68, blue = 55)
    val BROWN = Color(red = 92, green = 69, blue = 65)

    const val DIALOG_RADIUS = 12

    const val FAB_SEPARATOR = 12

    const val DATA_RETRIEVAL_PROGRESS = "Please wait, data retrieval in progress.."


    const val UNAVAILABLE_NAME = "#### ###"
    const val UNAVAILABLE_EMAIL = "################"





    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    object AuthErrors {
        const val CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
        const val EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    }


    const val JOB_DESCRIPTION = "JOB_DESCRIPTION"
    const val JOB_ROLE_WM = "JOB_ROLE"
    const val SAMPLE_WM = "SAMPLE_WM"
    const val COMPANY_NAME = "COMPANY_NAME"
    const val USER_DETAILS = "USER_DETAILS"
    const val USER_LOCATION = "USER_LOCATION"
    const val PROFILE_TYPE = "PROFILE_TYPE"


    const val STYLE_PROMPT = "STYLE_PROMPT"
    const val STYLE_RESUME_TEXT = "STYLE_RESUME_TEXT"
    const val STYLE_DATE_TIME_MILLI = "STYLE_DATE_TIME_MILLI"
    const val STYLE_RESUME_HISTORY = "STYLE_RESUME_HISTORY"
    const val RESUME_STYLE_ID = "RESUME_STYLE_ID"

    // Notification Channel constants

    // Name of Notification Channel for verbose notifications of background work
    val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
        "Verbose WorkManager Notifications"
    const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
        "Shows notifications whenever work starts"
    val NOTIFICATION_TITLE: CharSequence = "Generating Resume"
    const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_ID_SUPPORT = 2
    const val NOTIFICATION_SUPPORT_TITLE = "TailoR Support"//"Exception"
    const val NOTIFICATION_SUPPORT_MESSAGE = "An internal error occurred. Please try again or contact support for assistance."//Default Exception Type - (D.E.T)
    const val NOTIFICATION_SUPPORT_TITLE_I = "TailoR Support"//"Exception"
    const val NOTIFICATION_SUPPORT_MESSAGE_I = "An internal error occurred. Please try again or contact support for assistance."//IllegalStateException Exception Type - (I.E.T)
    const val DELAY_TIME_MILLIS: Long = 3000

    const val DEFAULT_HISTORY: Long = 10000000000000001
    const val DEFAULT_EDIT: Long = 10000000000000002
    const val DEFAULT_EDIT_COVER_LETTER: Long = 10000000000000003

    const val PROFILE_CARD_ELEVATION = 36


    const val GENERATE_FAILED = "GENERATE_FAILED"
    const val GENERATE_SUCCESS = "GENERATE_SUCCESS"

    const val YOUTUBE_LINK = "https://youtu.be/bfn8tDLFHTo"
    const val YOUTUBE_PLAYLIST = "https://www.youtube.com/playlist?list=PLG7FBbtsa22e5eKPRKg6U93QW0pMEk65M"
    const val YOUTUBE_LINK_CHANNEL = "https://www.youtube.com/channel/@Easit-Group"
    const val YOUTUBE_LINK_CIPHER = "https://youtu.be/60LIiRL0oo8"
    const val YOUTUBE_LINK_CREATE_PROFILE = "https://youtu.be/M2vM3ZNrotU"
    const val YOUTUBE_LINK_BASIC = "https://youtu.be/yaxU7WVCNHg"
    const val YOUTUBE_LINK_SAMPLE = "https://youtu.be/tQ8Jq_GicFQ"
    const val YOUTUBE_LINK_TAILORED = "https://youtu.be/0R4f1YGwlqA"
    const val YOUTUBE_LINK_STYLING = ""
    const val VOTING_LINK = "https://ai.google.dev/competition/projects/tailor"
    const val COFFEE_LINK = "https://buymeacoffee.com/Olaoluwa99"
    const val LINKED_IN_LINK = "https://www.linkedin.com/company/ai-tailor/"
    const val X_LINK = "https://x.com/Tailor_AI_"
    const val PDF_TO_WORD = "https://www.ilovepdf.com/pdf_to_word"
    const val MAIL = "tailor.ai.mail@gmail.com"
    const val DEEPLINK_DOMAIN = "easit.tailor.com"


    const val KEYWORDS_SHOW = "Show all keywords"
    const val KEYWORDS_HIDE = "Hide keywords"

    const val RESUME_SHOW = "Resume"
    const val COVER_LETTER_SHOW = "Cover letter"


    const val NO_LETTER_SHOW = "No Cover letter created for this resume."


    const val INVALID_INPUT_TEXT = "Invalid input. Please provide a job description."
    const val INVALID_INPUT_TEXT_ROLE = "Invalid input. Please provide a job role."

    const val HOME_COMPANY_NAME = "Company name"
    const val JOB_ROLE = "Job title"

    const val HOME_TAILORED_TITLE = "Enter the full job description for the role here."
    const val HOME_ROLE_TITLE = "Enter the job title you're applying for here."
    const val HOME_SAMPLE_TITLE = "Enter the required details here."
    const val HOME_STYLE_TITLE = "Select a Resume & Template then click 'Style'."
    const val HOME_EDIT_TITLE = "Select a resume, customize it, and click Modify."
    const val HOME_EDIT_CL_TITLE = "Select a resume, customize it's cover letter, and click Modify."

    const val PLACEHOLDER_COMPANY_NAME = "[PLACEHOLDER_COMPANY_NAME]"
    const val PLACEHOLDER_JOB_POSTING = "[JOB_POSTING_PLATFORM]"

    const val MAX_HISTORY_COUNT = 40

    const val STYLE_1 = 10001
    const val STYLE_2 = 10002
    const val STYLE_3 = 10003
    const val STYLE_4 = 10004
    const val STYLE_5 = 10005


    const val TAG_RES = "RES"
    const val TAG_BAS = "BAS"
    const val TAG_SAM = "SAM"
    const val TAG_TAL = "TAL"
    const val TAG_STY = "STY"
    const val TAG_EDR = "EDR"
    const val TAG_EDC = "EDC"


    const val INVALID_INPUT_CHARACTER = "Invalid characters detected: [ ] { & }. Please remove or replace them."

    const val NO_INVALID_INPUT = 999

    const val DEFAULT_BUILDER_SPACING = 8


}