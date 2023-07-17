package com.anpe.bilibiliandyou.network.data.model.suggest
import com.google.gson.annotations.SerializedName

data class SuggestEntityTest(
    @SerializedName("0")
    val x0: X0,
    @SerializedName("1")
    val x1: X0,
    @SerializedName("2")
    val x2: X0,
    @SerializedName("3")
    val x3: X0,
    @SerializedName("4")
    val x4: X0,
    @SerializedName("5")
    val x5: X0,
    @SerializedName("6")
    val x6: X0,
    @SerializedName("7")
    val x7: X0,
    @SerializedName("8")
    val x8: X0,
    @SerializedName("9")
    val x9: X0
)

data class X0(
    @SerializedName("name")
    val name: String,
    @SerializedName("ref")
    val ref: Int,
    @SerializedName("spid")
    val spid: Int,
    @SerializedName("term")
    val term: String,
    @SerializedName("value")
    val value: String
)