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
using System.IO;
using Microsoft.Xna.Framework;

namespace SecondScreen
{
    public class completeItem
    {
        public int id;
        public int stack;
        public string base64;
    }

    public class stackOnly
    {
        public int id;
        public int stack;
    }

    public class SecondScreen : Mod
    {
        public struct Inventory
        {
            public int id;
            public int stack;
            public string base64;

            public void set(int a, int b, string c)
            {
                id = a;
                stack = b;
                base64 = c;
            }
        }

        public static Socket socket = null;
        public long timer;
        public Inventory[] inventory = new Inventory[50];

        public SecondScreen()
        {
            Properties = new ModProperties()
            {
                Autoload = true,
                AutoloadGores = true,
                AutoloadSounds = true
            };
        }

        public override void Load()
        {
            for (int i = 0; i < 50; i++) inventory[i].set(0, 0, "");
            base.Load();
        }

        public override void PostDrawInterface(SpriteBatch spriteBatch)
        {
            if (Main.LocalPlayer.active && socket == null) {
                socket = IO.Socket("http://127.0.0.1:2222");
                socket.On("renew", (data) =>
                {
                    for (int i = 0; i < 50; i++) inventory[i].set(0, 0, "");
                });
                socket.On("move", (data) =>
                {
                    String s = data.ToString();
                    char[] array = s.ToCharArray();
                    int from = 0, to = 0, temp = 0;
                    bool first = true;
                    foreach (char c in array)
                    {
                        if (c > 47 && c < 58)
                        {
                            temp = (temp * 10) + c - 48;
                            if (first) from = temp;
                            else to = temp;
                        }
                        else if (c == ',')
                        {
                            first = false;
                            temp = 0;
                        }
                    }
                    if (from != to)
                    {
                        Item itemFrom = Main.LocalPlayer.inventory[from];
                        Item itemTo = Main.LocalPlayer.inventory[to];
                        Main.LocalPlayer.inventory[from] = itemTo;
                        Main.LocalPlayer.inventory[to] = itemFrom;
                    }
                });
                socket.On("use", (data) =>
                {
                    String s = data.ToString();
                    char[] array = s.ToCharArray();
                    int from = 0;
                    foreach (char c in array)
                    {
                        if (c > 47 && c < 58) from = (from * 10) + c - 48;
                    }
                    Item itemFrom = Main.LocalPlayer.inventory[from];
                    int val = 0;
                    if (Main.LocalPlayer.inventory[from].stack > 0 && Main.LocalPlayer.statLife > 0)
                    {
                        if (itemFrom.healLife > 0) {
                            val = itemFrom.healLife;
                            if (Main.LocalPlayer.statLife < Main.LocalPlayer.statLifeMax)
                            {
                                if (Main.LocalPlayer.statLifeMax - Main.LocalPlayer.statLife > val) Main.LocalPlayer.statLife += val;
                                else Main.LocalPlayer.statLife = Main.LocalPlayer.statLifeMax;
                                Main.LocalPlayer.inventory[from].stack--;
                            }
                        } else if (itemFrom.healMana > 0) {
                            val = itemFrom.healMana;
                            if (Main.LocalPlayer.statMana < Main.LocalPlayer.statManaMax)
                            {
                                if (Main.LocalPlayer.statManaMax - Main.LocalPlayer.statMana > val) Main.LocalPlayer.statMana += val;
                                else Main.LocalPlayer.statMana = Main.LocalPlayer.statManaMax;
                                Main.LocalPlayer.inventory[from].stack--;
                            }
                        }
                    }
                });
            }
            if (now() - timer > 500)
            {
                socket.Emit("playerInfo", "" + Main.myPlayer + Main.LocalPlayer.statLife + "," + Main.LocalPlayer.statLifeMax + "," + Main.LocalPlayer.statMana + "," + Main.LocalPlayer.statManaMax);
                for (int i = 0; i < 50; i++)
                {
                    if (Main.LocalPlayer.inventory[i].netID != inventory[i].id)
                    {
                        inventory[i].id = Main.LocalPlayer.inventory[i].netID;
                        inventory[i].stack = Main.LocalPlayer.inventory[i].stack;
                        inventory[i].base64 = getBase64String(Main.LocalPlayer.inventory[i]);
                        String ss = "" + i + "," + inventory[i].stack + "," + inventory[i].base64;
                        socket.Emit("completeItem", ss);
                    } else if (Main.LocalPlayer.inventory[i].stack != inventory[i].stack)
                    {
                        inventory[i].stack = Main.LocalPlayer.inventory[i].stack;
                        String ss = "" + i + "," + inventory[i].stack;
                        socket.Emit("stackOnly", ss);
                    }
                }
                timer = now();
            }
            base.PostDrawInterface(spriteBatch);
        }

        public long now()
        {
            return DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
        }

        public string getBase64String(Item i)
        {
            Stream stream = System.IO.File.Create("file.png");
            Main.itemTexture[i.netID].SaveAsPng(stream, Main.itemTexture[i.netID].Width, Main.itemTexture[i.netID].Height);
            stream.Dispose();
            return Convert.ToBase64String(System.IO.File.ReadAllBytes(@"file.png"));
        }
    }
}