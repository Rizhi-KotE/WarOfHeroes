require "json"
require "open-uri"

file = open "haven_creatures.crt"
json = JSON::parse file.read

domain = "http://heroes.ag.ru"

creatures = json.select { |creature| !creature["name"].empty? }

creatures.each do |creature|
  url = domain + creature["imageurl"]
  file = open url
  IO::write "#{creature['name']}.gif", file.read;
end