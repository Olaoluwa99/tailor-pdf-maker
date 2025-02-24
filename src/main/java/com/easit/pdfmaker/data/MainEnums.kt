package com.easit.pdfmaker.data

enum class ThemeColor{
    RED, GREEN, BLACK, BLUE, YELLOW, LIGHT_GRAY, DARK_GRAY
}

enum class SheetMode{
    DEFAULT, STYLE, COLOR, SECTION, SKILLS, SOFT_SKILLS, HOBBIES, LINK_COLOR
}

enum class StyleType{
    ALPHA, BETA, DELTA, GAMMA, OMEGA
}

enum class Sections{
    OBJECTIVE, EXPERIENCE, EDUCATION, PROJECT, SKILLS, SOFT_SKILLS, CERTIFICATIONS, HOBBIES
}

enum class ListFormat{
    FLOW_ROW, SINGLE_COLUMN, DOUBLE_COLUMN, TRIPLE_COLUMN
}

enum class ReloadState{
    INITIAL, ACTIVE, COMPLETED
}