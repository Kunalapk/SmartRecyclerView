package com.kunalapk.demo

class PriceComparator: Comparator<ModelData>{

    override fun compare(p0: ModelData?, p1: ModelData?): Int {
        if(p0 == null || p1 == null)
            return 0
        return p0.price.compareTo(p1.price)
    }
}