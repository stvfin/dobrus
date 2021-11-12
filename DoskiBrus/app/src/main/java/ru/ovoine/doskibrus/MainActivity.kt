package ru.ovoine.doskibrus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioGroup
import android.widget.Toast
import ru.ovoine.doskibrus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    var usl_prod: Int = 1;          var usl_nugno: Int = 1;   var itogo = 10000.0
    var shir = 100.0;  var vys = 50.0; var dlina = 6000.0; var nvol = 4.0;  var price = 15000.0
    var rez_m3 = 15000.0; var rez_m2 = 750.0; var rez_m = 75.0; var rez_dsk = 450.0
    var mypic: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypic = R.drawable.yug_430713_18
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.mainTb)
        bind.mainTb.subtitle = "Введите размеры доски и цену"
        //Слушатель радиогрупп с вызовом fun rglistener
        findViewById<RadioGroup>(R.id.rg_pr).setOnCheckedChangeListener { _r, chRBId -> rgListenerProd(chRBId) }
        findViewById<RadioGroup>(R.id.rg_nugno).setOnCheckedChangeListener { _r, chRBId -> rgListenerNugno(chRBId) }
        out_xml()
        raschet()
    }

    fun out_xml(){
        with(bind){
            etShirina.setText("$shir")
            etVysota.setText("$vys")
            etDlina.setText("$dlina")
            etPrice.setText("$price")
            etVol.setText("$nvol")
            tvRezsum.setText("$itogo")
            tvRezM3.setText("$rez_m3")
            tvRezM2.setText("$rez_m2")
            tvRezPm.setText("$rez_m")
            tvRezDsk.setText("$rez_dsk")
            rgPr.clearCheck()
            when(usl_prod){
                1 -> rgPr.check(R.id.rb_m3)
                2 -> rgPr.check(R.id.rb_m2)
                3 -> rgPr.check(R.id.rb_pm)
                4 -> rgPr.check(R.id.rb_dsk)
            }
            rgNugno.clearCheck()
            when(usl_nugno){
                1 -> rgNugno.check(R.id.rb_num3)
                2 -> rgNugno.check(R.id.rb_num2)
                3 -> rgNugno.check(R.id.rb_nupm)
                4 -> rgNugno.check(R.id.rb_nudsk)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(bind){
            shir = etShirina.getText().toString().toDouble()
            vys = etVysota.getText().toString().toDouble()
            dlina = etDlina.getText().toString().toDouble()
            nvol = etVol.getText().toString().toDouble()
            price = etPrice.getText().toString().toDouble()
        }
        outState.putInt("usl_prod",usl_prod)
        outState.putInt("usl_nugno",usl_nugno)
        outState.putDouble("shir",shir)
        outState.putDouble("vys",vys)
        outState.putDouble("dlina",dlina)
        outState.putDouble("price",price)
        outState.putDouble("nvol",nvol)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        usl_prod = savedInstanceState.getInt("usl_prod")
        usl_nugno = savedInstanceState.getInt("usl_nugno")
        shir = savedInstanceState.getDouble("shir")
        vys = savedInstanceState.getDouble("vys")
        dlina = savedInstanceState.getDouble("dlina")
        price = savedInstanceState.getDouble("price")
        nvol = savedInstanceState.getDouble("nvol")
        out_xml()
        raschet()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
//            android.R.id.home -> finish()
            R.id.ab_calc -> raschet()
            R.id.ab_info -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun rgListenerProd(checkedRBId: Int){
        var s: String? = null
        with(bind) {
            when (checkedRBId) {
                rbM3.id -> {
                    usl_prod = 1
                    s = getString(R.string.txt_m3)                }
                rbM2.id -> {
                    usl_prod = 2
                    s = getString(R.string.txt_m2)                }
                rbPm.id -> {
                    usl_prod = 3
                    s = getString(R.string.txt_m)                }
                rbDsk.id -> {
                    usl_prod = 4
                    s = getString(R.string.txt_dsk)                }
            }
            tvPrza.text="$s"
        }
    }

    fun rgListenerNugno(checkedRBId: Int){
        var s: String? = null
        with(bind) {
            when (checkedRBId) {
                rbNum3.id -> {
                    usl_nugno = 1
                    s = getString(R.string.txt_m3)                }
                rbNum2.id -> {
                    usl_nugno = 2
                    s = getString(R.string.txt_m2)                }
                rbNupm.id -> {
                    usl_nugno = 3
                    s = getString(R.string.txt_m)                }
                rbNudsk.id -> {
                    usl_nugno = 4
                    s = getString(R.string.txt_dsk)                }
            }
            tvNugnoza.text="$s"
        }
    }

    fun raschet(){
        with(bind){
            try {
                shir = etShirina.getText().toString().toDouble()
                vys = etVysota.getText().toString().toDouble()
                dlina = etDlina.getText().toString().toDouble()
                nvol = etVol.getText().toString().toDouble()
                price = etPrice.getText().toString().toDouble()
            }
            catch (e: NumberFormatException){
                Toast.makeText(this@MainActivity,"Ошибка ввода", Toast.LENGTH_SHORT).show()
            }
            if (shir==0.0 || vys==0.0 || dlina==0.0 || nvol==0.0 || price==0.0) {
                Toast.makeText(this@MainActivity,"Нулевые значения, проверьте", Toast.LENGTH_SHORT).show()
                return
            }
            val v = (shir*vys*dlina)/1000000000;    val s = (shir*dlina)/1000000;   val l = dlina/1000
            val nm3 = 1/v;                          val nm2 = 1/s;                  val nm = 1/l
            var prm3 = price;   var prm2 = price;   var prm = price;   var prdsk = price
            itogo = price
            when(usl_prod){
                1 ->{//m3
                    prm2 = prm3/(nm3*s)
                    prm = prm3/(nm3*l)
                    prdsk = prm3/nm3
                }
                2 ->{//m2
                    prm3 = prm2*(nm3*s)
                    prm = prm3/(nm3*l)
                    prdsk = prm3/nm3
                }
                3 ->{//m
                    prm3 = prm*(nm3*l)
                    prm2 = prm3/(nm3*s)
                    prdsk = prm3/nm3
                }
                4 ->{//dsk
                    prm3 = prdsk*nm3
                    prm = prm3/(nm3*l)
                    prm2 = prm3/(nm3*s)
                }
            }
            when(usl_nugno){
                1 -> itogo = prm3*nvol    //m3
                2 -> itogo = prm2*nvol    //m2
                3 -> itogo = prm*nvol     //m
                4 -> itogo = prdsk*nvol   //dsk
            }
            tvRezM3.text=String.format("%6.1f",prm3)
            tvRezM2.text=String.format("%6.1f",prm2)
            tvRezPm.text=String.format("%6.1f",prm)
            tvRezDsk.text=String.format("%6.1f",prdsk)
            tvRezsum.text=String.format("%6.1f",itogo)
        }
    } //fun raschet
}