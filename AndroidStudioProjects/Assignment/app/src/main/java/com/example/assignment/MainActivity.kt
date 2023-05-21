package com.example.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // #NO.1
        //val expression = "100 / 1"
        //val expression = "100 + 30"
        //val expression = "100 * 20"
        //val expression = "100 % 20"
        val expression = "100 / 0"
        val expressionList = expression.split(" ")  //공백을 기준으로 split

        val num1 = Integer.parseInt(expressionList[0])          //첫번째 index의 값은 num1
        val sign = expressionList[1]                            //두번째 index의 값은 사칙연산 기호
        val num2 = Integer.parseInt(expressionList[2])          //세번째 index의 값은 num2

        val result = when (sign) {      //입력되는 기호에 따라 다른 계산 방식 및 결과
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "%" -> num1 % num2
            "/" -> {                    //나눗셈은 num2에 0이 들어오면 나눌 수 없음을 표현
                if (num2 == 0) {
                    "cannot divide by zero"
                } else {
                    num1 / num2
                }
            }
            else -> Log.d("HW01", "cannot calculate Expression")
        }
        Log.d("HW01", "Result: $result (expression: $expression)")


        // #No.2
        //val testNum =95;
        val testNum = 99;
        val ten = testNum / 10     //testNum의 10의 자리값
        val one = testNum % 10     //testNum의 1의 자리값

        if (ten == one) {
            Log.d("HW01", "Yes! two numbers are same! (Number: ${testNum})")
        } else {
            Log.d("HW01", "No! two numbers are NOT same! (Number: ${testNum})")
        }   //10의 자리와 1의 자리가 같으면 Yes문, 다르면 No문 출력


        // #No.3
        val capacity = 20                           //배열 크기 생성
        val myArray = mutableSetOf<Int>()           //배열에 중복된 번호가 없도록 mutableSetOf

        while (myArray.size < capacity) {
            myArray.add(Random().nextInt(100))        //Random()을 이용해 1부터 100까지의 정수 랜덤 생성
        }
        Log.d("HW01", "result: $myArray, capacity: $capacity")


        // #No.4
        //문자열 예시
        val strLine = "Seoul National University of Science and Technology"
        //val strLine = "Seoul Station"
        //val strLine = "Industrial Information Systems and Engineering"
        //val strLine = "Android and Kotlin is not that difficult"
        //val strLine = "Exit"

        val strList = strLine.split(" ")                        // 공백을 기준으로 단어 split
        Log.d("HW01", "The number of word is ${strList.size}")  //split 된 단어 개수 출력


        // #No.5
        var str = "I Love Kotlin"

        Log.d("HW01", str)                  //원래 문자열 출력
        for (i in str.indices) {                  //문자열의 첫번째 문자를 새로운 변수에 저장하고 첫번째 문자를 지운 뒤 새로운 변수와 더해 문자열 생성
            var last = str.first()
            str = str.drop(1) + last
            Log.d("HW01", str)
        }


        // #No.6
        val str = "jinwoo"
        //val str = "seoul"
        //val str = "DDADD"
        //val str = "WOWOW"

        val reverseStr = str.reversed()     //거꾸로 된 문자열 생성
        if (str == reverseStr) {
            Log.d("HW01", "$str is palindrome!")
        } else {
            Log.d("HW01", "$str is Not palindrome!")
        }   //기존 문자열과 거꾸로 바꾼 문자열이 같으면 palindroem 다르면 Not palindrome 출력


        // #No.7
        val sequence = "abcabcdefabc"

        val alphabet = mutableMapOf<Char, Int>()
        for (i in sequence){                                //주어진 문자열에 몇개의 알파벳이 있는지 카운팅
            val count = alphabet.getOrDefault(i,0)
            alphabet[i] = count + 1
        }
        val sorted = alphabet.toSortedMap()           //출력을 위한 오름차순 정렬
        for ((key, value) in sorted){
            Log.d("HW01", "$key: $value")         //각 key값과 value값 출력
        }


        // #No.8
        data class Item(val name : String){
            init{ Log.d("HW01","${this.name} item was created")} //새로운 인스턴스 생성될 때마다 log 출력

            // price값과 share의 값 얻어주고 getter와 setter를 이용해 log 출력
            var price : Int = 0
                set(value){
                    Log.d("HW01","price set to $value. Are you serious?")
                    field = value
                }
                get() = field

            var share : Int = 0
                get() = field
        }
        val item1 = Item(name="jinwoo1").apply{
            share = 100
            price = 500
        }

        // #No.9
        var itemList = mutableListOf<Item>()        //itemList 생성

        for (i in 0..9){                      //for문을 활용해 list에 item 추가
            val sampleItem = Item("SeungJun${i}").apply{    //본인이름+iteration number
                share = 100                         //share field 100으로 지정
                price = Random().nextInt(1000)  //price 0~1000사이의 random값 지정
            }
            itemList.add(sampleItem)
        }
        itemList.forEach{Log.d("HW01","name: ${it.name} price: ${it.price}")}
        //itemList에 있는 이름과 가격 log 출력


        // #No.10
        var price500 = itemList.filter{it.price>=500}           //filter메소드 활용하여 price가 500이상 추출
        Log.d("HW01","$price500")


        // #No.11
        itemList.run{           //run scope function으로 price기준 오름차순 정렬
            sortBy {it.price}
            toString()          //정렬된 것 문자열 변환
        }.also{Log.d("HW01","[${it.uppercase()}]")}  //also function에서 대문자 변환 후 log 출력
    }
}