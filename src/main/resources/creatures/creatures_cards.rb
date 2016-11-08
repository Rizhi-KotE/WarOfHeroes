#!/usr/bin/ruby
require "open-uri"
require "nokogiri"
require "json"
require "./map_rus_to_eng"

  @races_names = ['sylvan', 'inferno', 'haven']
  @names_to_rus = {'sylvan' => 'Лесной Союз',
                  'inferno' => 'Инферно',
                  'haven' => 'Оплот Порядка'}

  @domain = "http://heroes.ag.ru"

  def collect_creatures_properties(url)
    html = Nokogiri::HTML open url
    cards = html.css ".tc"

    creatures = cards.map do |card|
      creature = {}
      header = card.css ".cbg"
      creature["name"] = /\((.*)\)/.match(header.text).to_s
      creature["imageurl"] = card.css(".big_creature").attr("src").to_s
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

    creatures = creatures.select do |creature|
        creature['name'].length > 1
    end.map do |creature|
      creature['name'].gsub!(/\((.*)\)/) {$1}
      creature['image'] = "/creatures/#{creature['name']}.gif"
      map_to_eng creature
    end
  end

  def collect_race_properties(race)
    race_json = {};
    race_json['name'] = @names_to_rus[race]
    race_json['image'] = "creatures/#{race}.gif"
    race_json['creatures'] = collect_creatures_properties("#{@domain}/heroes5/creatures/#{race}")
    race_json
  end

@races_names.each { |race| IO::write "#{race}.crt", collect_race_properties(race).to_json }