package com.plcoding.jetpackcomposepokedex.util

sealed class Resosre<T>(val data:T?=null,val Messeg:String?=null)
{
    class  succses<T>( data:T): Resosre<T>(data)
    class  error<T>( Messeg:String,data:T?=null): Resosre<T>(data,Messeg)
   class  Loading<T>: Resosre<T>()
}

