class CreateBtcHighMarket < ActiveRecord::Migration
  def change
    create_table :btc_high_market do |t|
      t.datetime :time,:default => '2199-12-31 00:00:00'
      t.integer :type
    end
  end
end