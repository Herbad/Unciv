package com.unciv.models.gamebasics

import com.unciv.ui.utils.tr
import java.util.*

class Technology : ICivilopedia {
    override val description: String
        get(){
            val SB=StringBuilder()
            if(baseDescription!=null) SB.appendln(baseDescription)


            val improvedImprovements = GameBasics.TileImprovements.values.filter { it.improvingTech==name }.groupBy { it.improvingTechStats.toString() }
            improvedImprovements.forEach{
                val impimpString = it.value.joinToString { it.name.tr() } +" provide" + (if(it.value.size==1) "s" else "") +" "+it.key
                SB.appendln(impimpString)
            }

            val enabledUnits = GameBasics.Units.values.filter { it.requiredTech==name }
            if(enabledUnits.isNotEmpty()) SB.appendln("" +
                    "Units enabled: "+enabledUnits.map { it.name + " ("+it.getShortDescription()+")" }.joinToString())

            val enabledBuildings = GameBasics.Buildings.values.filter { it.requiredTech==name }
            val regularBuildings = enabledBuildings.filter { !it.isWonder }
            if(regularBuildings.isNotEmpty())
                SB.appendln("Buildings enabled: "+regularBuildings.map { "\n * "+it.name + " ("+it.getShortDescription()+")" }.joinToString())
            val wonders = enabledBuildings.filter { it.isWonder }
            if(wonders.isNotEmpty()) SB.appendln("Wonders enabled: "+wonders.map { "\n * "+it.name+ " ("+it.getShortDescription()+")" }.joinToString())

            val revealedResource = GameBasics.TileResources.values.filter { it.revealedBy==name }.map { it.name.tr() }.firstOrNull() // can only be one
            if(revealedResource!=null) SB.appendln("Reveals $revealedResource on map")

            val tileImprovements = GameBasics.TileImprovements.values.filter { it.techRequired==name }
            if(tileImprovements.isNotEmpty()) SB.appendln("Tile improvements enabled: "+tileImprovements.map { it.name.tr() }.joinToString())

            return SB.toString().trim()
        }
    lateinit var name: String

    var baseDescription: String? = null
    var cost: Int = 0
    var prerequisites = HashSet<String>()

    var column: TechColumn? = null // The column that this tech is in the tech tree
    var row: Int = 0

    override fun toString(): String {
        return name
    }
}