package com.equitypulse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsSentimentResponse(
    @SerializedName("feed") val feed: List<NewsDto>,
    @SerializedName("items") val items: String,
    @SerializedName("sentiment_score_definition") val sentimentScoreDefinition: String? = null,
    @SerializedName("relevance_score_definition") val relevanceScoreDefinition: String? = null
) 