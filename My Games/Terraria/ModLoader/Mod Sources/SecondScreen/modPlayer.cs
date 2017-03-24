using Terraria;
using Terraria.Graphics.Effects;
using Terraria.Graphics.Shaders;
using Terraria.ID;
using Terraria.ModLoader;
using System.Net;
using System.Text;
using Quobject.SocketIoClientDotNet.Client;
using System;
using Microsoft.Xna.Framework.Graphics;
using System.Collections.Generic;
using System.IO;
using Microsoft.Xna.Framework;
using Terraria.DataStructures;
using Terraria.ModLoader.IO;
using Terraria.GameInput;

namespace SecondScreen
{
    public class modPlayer : ModPlayer
    {
        public int score = 0;
        
        public override TagCompound Save()
        {
            return new TagCompound {
                {"score", score}
            };
        }

        public override void Load(TagCompound tag)
        {
            score = tag.GetInt("score");
        }
    }
}
