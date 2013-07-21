package com.podevs.android.pokemononline.poke;

import com.podevs.android.pokemononline.pokeinfo.HiddenPowerInfo;
import com.podevs.android.utilities.Bais;
import com.podevs.android.utilities.Baos;
import com.podevs.android.utilities.SerializeBytes;

// This class is how a poke is represented in the teambuilder.
public class TeamPoke implements SerializeBytes, Poke {
	public UniqueID uID;
	public String nick;
	public short item;
	public short pokeball;
	public short ability;
	public byte nature;
	public byte gender;
	public Gen gen;
	public boolean shiny;
	public byte happiness;
	public byte level;
	public TeamMove[] moves = new TeamMove[4];
	public byte[] DVs = new byte[6];
	public byte[] EVs = new byte[6];
	
	public TeamPoke(Bais msg) {
		loadFromBais(msg);
	}
	
	public TeamPoke(Bais msg, Gen gen) {
		this.gen = gen;
		loadFromBais(msg);
	}
	
	public void loadFromBais(Bais msg) {
		Bais b = new Bais(msg.readVersionControlData());
		int version = b.read();
		
		if (version != 0) {
		
		}
		
		Bais network = b.readFlags();

//		hasGen, hasNickname, hasPokeball, hasHappiness, hasPPups, hasIVs,
//      isShiny=0
		if (network.readBool()) { // gen flag
			gen = new Gen(b);
		} else if (gen == null) {
			gen = new Gen();
		}
		uID = new UniqueID(b);
		level = b.readByte();
		
		Bais data = b.readFlags();
		shiny = data.readBool();
		
		if (network.readBool()) { //nickname flag
			nick = b.readString();
		} else {
			nick = "";
		}

		if (network.readBool()) { //pokeball flag
			pokeball = b.readShort();
		} else {
			pokeball = 0;
		}
		
		if (gen.num > 1) {
			item = b.readShort();
			if (gen.num > 2) {
				ability = b.readShort();
				nature = b.readByte();
			}

			gender = b.readByte();
			if (gen.num > 2 && network.readBool()) { //happiness flag
				happiness = b.readByte();
			}
		}
		
		boolean ppups = network.readBool(); //ppup flags
		for (int i = 0; i < 4; i++) {
			if (ppups) {
				b.readByte(); // read the pp up for the move, but ignore it
			}
			moves[i] = new TeamMove(b.readShort());
		}
		
		for(int i = 0; i < 6; i++)
			EVs[i] = b.readByte();
		
		if (network.readBool()) { //Ivs flags
			for(int i = 0; i < 6; i++)
				DVs[i] = b.readByte();
		} else {
			DVs[0] = DVs[1] = DVs[2] = DVs[3] = DVs[4] = DVs[5] = 31;
		}
	}
	
	public TeamPoke() {
		uID = new UniqueID();
		nick = "";
		item = 71;
		ability = 98;
		nature = 0;
		gender = 1;
		gen = new Gen();
		shiny = true;
		happiness = 0;
		level = 100;
		/*moves[0] = 331;
		moves[1] = 213;
		moves[2] = 412;
		moves[3] = 210;*/
		moves[0] = new TeamMove(118);
		moves[1] = new TeamMove(227);
		moves[2] = new TeamMove(150);
		moves[3] = new TeamMove(271);
		DVs[0] = DVs[1] = DVs[2] = DVs[3] = DVs[4] = DVs[5] = 31;
		EVs[0] = EVs[1] = EVs[2] = EVs[3] = EVs[4] = EVs[5] = 10;
	}
	
	public void serializeBytes(Baos bytes) {
		Baos b = new Baos();
//		hasGen, hasNickname, hasPokeball, hasHappiness, hasPPups, hasIVs,
//      isShiny=0
		b.putFlags(new boolean[]{false, nick.length() > 0, false, happiness != 0, false, true});
		
		b.putBaos(uID);
		b.write(level);
		b.write(shiny ? 1 : 0);
		
		if (nick.length() > 0) {
			b.putString(nick);
		}
		
		if (gen.num > 1) {
			b.putShort(item);
			
			if (gen.num > 2) {
				b.putShort(ability);
				b.write(nature);
			}
		
			b.write(gender);
		
			if (gen.num > 2 && happiness != 0) {
				b.write(happiness);
			}
		}
		
		for (int i = 0; i < 4; i++) {
			b.putShort(moves[i].num);
		}
		
		for (int i = 0; i < 6; i++) b.write(EVs[i]);
		for (int i = 0; i < 6; i++) b.write(DVs[i]);
		
		bytes.putVersionControl(0, b);
	}

	public int ability() {
		return ability;
	}

	public int item() {
		return item;
	}

	public int totalHP() {
		return 0;
	}

	public int currentHP() {
		return 0;
	}

	public CharSequence nick() {
		return nick;
	}

	public UniqueID uID() {
		return uID;
	}

	public Move move(int j) {
		return moves[j];
	}

	public int hiddenPowerType() {
		return HiddenPowerInfo.hiddenPowerType(this);
	}

	public int dv(int i) {
		return DVs[i];
	}
}
