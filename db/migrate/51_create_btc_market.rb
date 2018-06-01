class CreateBtcMarket < ActiveRecord::Migration
  def change
    create_table :btc_market do |t|
      t.datetime :time,:default => '2199-12-31 00:00:00'
      t.string :name
      t.string :open
      t.string :hight
      t.string :low
      t.string :close
      t.string :volume
      t.string :quote_volume
      t.string :taker_buy_base_asset_volume
      t.string :taker_buy_quote_asset_volume
      t.string :trade_num
      t.integer :type

    end
  end
end