require "json"
require "open-uri"

@races_names = ['sylvan', 'inferno', 'haven']

def upload_race_creature_images(race)
  file = open "#{race}.crt"
  json = JSON::parse file.read

  domain = "http://heroes.ag.ru"

  creatures = json['creatures']

  creatures.each do |creature|
    url = domain + creature["imageurl"]
    file = open url
    IO::write "#{creature['name']}.gif", file.read;
  end
end

def upload_race_image (race)
  url = "http://heroes.ag.ru/heroes5/towns/pic/#{race}.gif"
  file = open url
  IO::write "#{race}.gif", file.read;
end

@races_names.each do |race|
  upload_race_creature_images race
  upload_race_image race
end