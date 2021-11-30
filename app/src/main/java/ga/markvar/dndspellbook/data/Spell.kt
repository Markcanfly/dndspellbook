package ga.markvar.dndspellbook.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spell")
data class Spell (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "area_of_effect")    var areaOfEffect:   String?,
    @ColumnInfo(name = "attack_type")       var attackType:     String?,
    @ColumnInfo(name = "casting_time")      var castingTime:    String,
    @ColumnInfo(name = "components")        var components:     String,
    @ColumnInfo(name = "classes")           var classes:        String,
    @ColumnInfo(name = "concentration")     var concentration:  Boolean,
    @ColumnInfo(name = "damage")            var damage:         String?,
    @ColumnInfo(name = "dc")                var dc:             String?,
    @ColumnInfo(name = "description")       var description:    String,
    @ColumnInfo(name = "duration")          var duration:       String,
    @ColumnInfo(name = "heal_at_slot_level")var healAtSlotLevel:String?,
    @ColumnInfo(name = "higher_level")      var higherLevel:    String?,
    @ColumnInfo(name = "index_name")        var index_name:     String,
    @ColumnInfo(name = "level")             var level:          Int,
    @ColumnInfo(name = "material")          var material:       String?,
    @ColumnInfo(name = "name")              var name:           String,
    @ColumnInfo(name = "range")             var range:          String,
    @ColumnInfo(name = "ritual")            var ritual:         Boolean,
    @ColumnInfo(name = "school")            var school:         String,
    @ColumnInfo(name = "url")               var url:            String
)
