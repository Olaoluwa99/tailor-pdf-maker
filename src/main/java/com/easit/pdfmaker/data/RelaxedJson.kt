package com.easit.pdfmaker.data

import kotlinx.serialization.json.Json

val relaxedJson = Json {
    ignoreUnknownKeys = true      // Useful during **deserialization** only
    isLenient = true              // Loosens rules for parsing (e.g., allows unquoted keys, etc.)
    coerceInputValues = true      // Uses default values when fields are missing or null (deserialize only)
    //prettyPrint = true            // Affects **serialization** (nicely formatted output)
}