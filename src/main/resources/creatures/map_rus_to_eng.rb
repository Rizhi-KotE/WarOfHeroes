


def map_to_eng json
	attr_map = {"атака":"attack", 
	"скорость":"speed",
	"защита":"defence",
	"инициатива":"initiative",
	"урон":"damage",
	"выстрелы":"shoots",
	"здоровье":"health",
	"мана":"mana"}
	p json
		attr_map.each do |key, value|
			json[value] = json[key.to_s]
		end
	json
end