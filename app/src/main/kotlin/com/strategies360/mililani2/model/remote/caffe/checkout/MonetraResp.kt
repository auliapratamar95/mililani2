package com.strategies360.mililani2.model.remote.caffe.checkout

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "MonetraResp", strict = false)
data class MonetraResp (
  @field:Element(name = "DataTransferStatus")
  @param:Element(name = "DataTransferStatus")
  val dataTransferStatus: DataTransferStatus,

  @field:Element(name = "Resp")
  @param:Element(name = "Resp")
  val resp: Resp
)

data class DataTransferStatus (
  @field:Attribute(name = "code")
  @param :Attribute(name = "code")
  val code: String
)

data class Resp (
  @field:Element(name = "cardtype")
  @param:Element(name = "cardtype")
  val cardtype: String,

  @field:Element(name = "code")
  @param:Element(name = "code")
  val code: String,

  @field:Element(name = "monetra_resp_hmacsha256")
  @param:Element(name = "monetra_resp_hmacsha256")
  val monetraRespHmacsha256: String,

  @field:Element(name = "msoft_code")
  @param:Element(name = "msoft_code")
  val msoftCode: String,

  @field:Element(name = "pclevel")
  @param:Element(name = "pclevel")
  val pclevel: String,

  @field:Element(name = "phard_code")
  @param:Element(name = "phard_code")
  val phardCode: String,

  @field:Element(name = "ticket")
  @param:Element(name = "ticket")
  val ticket: String,

  @field:Element(name = "verbiage")
  @param:Element(name = "verbiage")
  val verbiage: String,

  @field:Attribute(name = "identifier")
  @param:Attribute(name = "identifier")
  val identifier: String
)