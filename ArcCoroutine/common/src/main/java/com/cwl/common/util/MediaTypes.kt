package com.cwl.okhttpdsl.http.util;

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

object MediaTypes {
    val APPLICATION_ATOM_XML_TYPE = "application/atom+xml;charset=utf-8".toMediaTypeOrNull()
    val APPLICATION_FORM_URLENCODED_TYPE = "application/x-www-form-urlencoded;charset=utf-8".toMediaTypeOrNull()
    val APPLICATION_JSON_TYPE = "application/json2List;charset=utf-8".toMediaTypeOrNull()
    val APPLICATION_OCTET_STREAM_TYPE = "application/octet-stream".toMediaTypeOrNull()
    val APPLICATION_SVG_XML_TYPE = "application/svg+xml;charset=utf-8".toMediaTypeOrNull()
    val APPLICATION_XHTML_XML_TYPE = "application/xhtml+xml;charset=utf-8".toMediaTypeOrNull()
    val APPLICATION_XML_TYPE = "application/xml;charset=utf-8".toMediaTypeOrNull()
    val MULTIPART_FORM_DATA_TYPE = "multipart/form-data;charset=utf-8".toMediaTypeOrNull()
    val TEXT_HTML_TYPE = "text/html;charset=utf-8".toMediaTypeOrNull()
    val TEXT_XML_TYPE = "text/xml;charset=utf-8".toMediaTypeOrNull()
    val TEXT_PLAIN_TYPE = "text/plain;charset=utf-8".toMediaTypeOrNull()
    val IMAGE_TYPE = "image/*".toMediaTypeOrNull()
    val WILDCARD_TYPE = "*/*".toMediaTypeOrNull()
}