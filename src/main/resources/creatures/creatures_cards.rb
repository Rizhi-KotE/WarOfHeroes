#!/usr/bin/ruby
require "open-uri"
require "nokogiri"
require "json"
require "./map_rus_to_eng"

html = Nokogiri::HTML open "http://heroes.ag.ru/heroes5/creatures/inferno/"

cards = html.css ".tc"

creatures = cards.map do |card|
	creature = {}
	header = card.css ".cbg"
	creature["name"] =  /\((.*)\)/.match(header.text).to_s
	creature["imageurl"] = card.css(".big_creature").attr("src").to_s
	creature["image"] = "/creatures/#{creature['name']}.gif"
	trs = card.css(".table").css("td")
	alt = nil;
	tds_pairs = trs.map do |tr|
			if alt == nil
				alt = tr.css("img").attr("alt").to_s
			else
				creature[alt] = tr.text.to_i
				alt = nil
		end
	end
	creature
end

creatures = creatures.map do |creatures| 
	map_to_eng creatures
end

IO::write "inferno_creatures.crt", creatures.to_json