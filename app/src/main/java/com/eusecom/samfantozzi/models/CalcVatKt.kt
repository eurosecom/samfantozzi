package com.eusecom.samfantozzi

data class CalcVatKt(var zk0 : Double, var zk1 : Double, var zk2 : Double, var dn1 : Double, var dn2 : Double,
                     var hod : Double, var nod : Double, var winp: Int, var logprx: Boolean ){


    fun setSumnod() {

        val sumhod: Double = zk0 + zk1 + zk2 + dn1 + dn2
        this.nod = sumhod

    }

}
